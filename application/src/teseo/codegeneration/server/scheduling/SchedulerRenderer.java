package teseo.codegeneration.server.scheduling;

import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.cron.CronTrigger;
import teseo.helpers.Commons;
import teseo.scheduled.ScheduledTrigger;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class SchedulerRenderer {
	private final List<ScheduledTrigger> triggers;

	public SchedulerRenderer(Graph graph) {
		triggers = graph.find(ScheduledTrigger.class);
	}

	public void execute(File destiny, String packageName) {
		if (triggers.isEmpty()) return;
		Frame frame = new Frame().addTypes("scheduler");
		frame.addSlot("package", packageName);
		frame.addSlot("schedule", (AbstractFrame[]) processTriggers(triggers));
		Commons.writeFrame(destiny, "Schedules", template().format(frame));
	}

	private Frame[] processTriggers(List<ScheduledTrigger> triggers) {
		return triggers.stream().map(this::processTrigger).toArray(Frame[]::new);
	}

	private Frame processTrigger(ScheduledTrigger scheduledTrigger) {
		final Frame schedule = new Frame().addTypes("schedule").addSlot("name", scheduledTrigger.name());
		schedule.addTypes(scheduledTrigger.getClass().getSimpleName());
		final Frame triggerFrame = new Frame().addTypes("trigger").addSlot("name", scheduledTrigger.id());
		triggerFrame.addTypes(scheduledTrigger.getClass().getSimpleName());
		if (scheduledTrigger.is(CronTrigger.class)) {
			final CronTrigger cron = scheduledTrigger.as(CronTrigger.class);
			triggerFrame.addTypes("cron")
					.addSlot("pattern", cron.pattern())
					.addSlot("mean", cron.mean());
		}
		schedule.addSlot("trigger", triggerFrame);
		return schedule;
	}

	private Template template() {
		Template template = SchedulerTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}
}
