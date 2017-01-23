package io.intino.pandora.builder.codegeneration.server.task;

import io.intino.pandora.builder.codegeneration.Formatters;
import io.intino.pandora.model.Task;
import io.intino.pandora.model.crontrigger.CronTriggerTask;
import io.intino.pandora.model.directorysentinel.DirectorySentinelTask;
import io.intino.pandora.model.scheduled.ScheduledTask;
import io.intino.pandora.builder.helpers.Commons;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import io.intino.tara.magritte.Graph;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskerRenderer {
	private final List<Task> tasks;
	private final File gen;
	private final String packageName;
	private final String boxName;

	public TaskerRenderer(Graph graph, File gen, String packageName, String boxName) {
		tasks = graph.find(Task.class);
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		if (tasks.isEmpty()) return;
		Frame frame = new Frame().addTypes("tasker");
		frame.addSlot("package", packageName);
		frame.addSlot("box", boxName);
		frame.addSlot("task", (AbstractFrame[]) processTasks(tasks));
		Commons.writeFrame(gen, "Tasks", template().format(frame));
	}

	private Frame[] processTasks(List<Task> tasks) {
		List<Frame> list = new ArrayList<>();
		list.addAll(tasks.stream().filter(t -> t.is(ScheduledTask.class)).map(t -> t.as(ScheduledTask.class)).map(this::processTask).collect(Collectors.toList()));
		list.addAll(tasks.stream().filter(t -> t.is(DirectorySentinelTask.class)).map(t -> t.as(DirectorySentinelTask.class)).map(this::processDirectorySentinel).collect(Collectors.toList()));
		return list.toArray(new Frame[list.size()]);
	}

	private Frame processTask(ScheduledTask task) {
		final Frame schedule = new Frame().addTypes("task").addTypes(task.getClass().getSimpleName()).
				addSlot("name", task.name());
		final Frame jobFrame = new Frame().addTypes("job").addTypes(task.getClass().getSimpleName())
				.addSlot("name", task.id());
		if (task.is(CronTriggerTask.class)) {
			final CronTriggerTask cron = task.as(CronTriggerTask.class);
			jobFrame.addTypes("cronTrigger")
					.addSlot("pattern", cron.pattern())
					.addSlot("mean", cron.mean());
		}
		schedule.addSlot("job", jobFrame);
		return schedule;
	}

	private Frame processDirectorySentinel(DirectorySentinelTask task) {
		final Frame sentinel = new Frame().addTypes("task").addTypes(task.getClass().getSimpleName());
		sentinel.addSlot("event", task.events().stream().map(Enum::name).toArray(String[]::new));
		sentinel.addSlot("file", task.directory() == null ? "" : task.directory().getPath());
		sentinel.addSlot("name", task.name());
		sentinel.addSlot("package", packageName);
		return sentinel;
	}

	private Template template() {
		return Formatters.customize(TaskerTemplate.create());
	}
}
