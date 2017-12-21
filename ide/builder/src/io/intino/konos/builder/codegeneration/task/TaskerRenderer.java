package io.intino.konos.builder.codegeneration.task;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Task;
import io.intino.konos.model.graph.boottrigger.BootTriggerTask;
import io.intino.konos.model.graph.crontrigger.CronTriggerTask;
import io.intino.konos.model.graph.directorysentinel.DirectorySentinelTask;
import io.intino.konos.model.graph.scheduled.ScheduledTask;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskerRenderer {
	private final List<Task> tasks;
	private final File gen;
	private final String packageName;
	private final String boxName;

	public TaskerRenderer(KonosGraph graph, File gen, String packageName, String boxName) {
		this.tasks = graph.taskList();
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
		list.addAll(tasks.stream().filter(t -> t.i$(ScheduledTask.class)).map(t -> t.a$(ScheduledTask.class)).map(this::processTask).collect(Collectors.toList()));
		list.addAll(tasks.stream().filter(t -> t.i$(DirectorySentinelTask.class)).map(t -> t.a$(DirectorySentinelTask.class)).map(this::processDirectorySentinel).collect(Collectors.toList()));
		return list.toArray(new Frame[list.size()]);
	}

	private Frame processTask(ScheduledTask task) {
		final Frame schedule = new Frame().addTypes("task").addTypes(task.getClass().getSimpleName()).
				addSlot("name", task.name$());
		final Frame jobFrame = new Frame().addTypes("job").addTypes(task.getClass().getSimpleName())
				.addSlot("name", task.core$().id());
		if (task.i$(CronTriggerTask.class)) {
			final CronTriggerTask cron = task.a$(CronTriggerTask.class);
			jobFrame.addTypes("cronTrigger")
					.addSlot("pattern", cron.pattern())
					.addSlot("mean", cron.mean());
		} else if (task.i$(BootTriggerTask.class)) {
			jobFrame.addTypes("onBootTrigger");
		}
		schedule.addSlot("job", jobFrame);
		return schedule;
	}

	private Frame processDirectorySentinel(DirectorySentinelTask task) {
		final Frame sentinel = new Frame().addTypes("task").addTypes(task.getClass().getSimpleName());
		sentinel.addSlot("event", task.events().stream().map(Enum::name).toArray(String[]::new));
		sentinel.addSlot("file", task.directory() == null ? "" : task.directory());
		sentinel.addSlot("name", task.name$());
		sentinel.addSlot("package", packageName);
		return sentinel;
	}

	private Template template() {
		return Formatters.customize(TaskerTemplate.create());
	}
}
