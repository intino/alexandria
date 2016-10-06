package io.intino.pandora.plugin.codegeneration.server.scheduling;

import io.intino.pandora.plugin.crontrigger.CronTriggerTask;
import io.intino.pandora.plugin.helpers.Commons;
import io.intino.pandora.plugin.scheduled.ScheduledTask;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class SchedulerRenderer {
    private final List<ScheduledTask> tasks;
    private final File gen;
    private final String packageName;

    public SchedulerRenderer(Graph graph, File gen, String packageName) {
        tasks = graph.find(ScheduledTask.class);
        this.gen = gen;
        this.packageName = packageName;
    }

    public void execute() {
        if (tasks.isEmpty()) return;
        Frame frame = new Frame().addTypes("scheduler");
        frame.addSlot("package", packageName);
        frame.addSlot("schedule", (AbstractFrame[]) processTasks(tasks));
        Commons.writeFrame(gen, "Schedules", template().format(frame));
    }

    private Frame[] processTasks(List<ScheduledTask> tasks) {
        return tasks.stream().map(this::processTask).toArray(Frame[]::new);
    }

    private Frame processTask(ScheduledTask task) {
        final Frame schedule = new Frame().addTypes("schedule").addSlot("name", task.name());
        schedule.addTypes(task.getClass().getSimpleName());
        final Frame taskFrame = new Frame().addTypes("task").addSlot("name", task.id());
        taskFrame.addTypes(task.getClass().getSimpleName());
        if (task.is(CronTriggerTask.class)) {
            final CronTriggerTask cron = task.as(CronTriggerTask.class);
            taskFrame.addTypes("cronTrigger")
                    .addSlot("pattern", cron.pattern())
                    .addSlot("mean", cron.mean());
        }
        schedule.addSlot("task", taskFrame);
        return schedule;
    }

    private Template template() {
        Template template = SchedulerTemplate.create();
        template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
        template.add("ValidPackage", Commons::validPackage);
        return template;
    }
}
