package io.intino.konos.builder.codegeneration.task;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Task;
import io.intino.konos.model.graph.boottrigger.BootTriggerTask;
import io.intino.konos.model.graph.crontrigger.CronTriggerTask;
import io.intino.konos.model.graph.directorysentinel.DirectorySentinelTask;
import io.intino.konos.model.graph.scheduled.ScheduledTask;

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
		Commons.writeFrame(gen, "Tasks", template().render(
				new FrameBuilder("scheduler")
						.add("package", packageName)
						.add("box", boxName)
						.add("task", processTasks(tasks)).toFrame()));
	}

	private Frame[] processTasks(List<Task> tasks) {
		List<Frame> list = new ArrayList<>();
		list.addAll(tasks.stream().filter(t -> t.i$(ScheduledTask.class)).map(t -> t.a$(ScheduledTask.class)).map(this::processTask).collect(Collectors.toList()));
		list.addAll(tasks.stream().filter(t -> t.i$(DirectorySentinelTask.class)).map(t -> t.a$(DirectorySentinelTask.class)).map(this::processDirectorySentinel).collect(Collectors.toList()));
		return list.toArray(new Frame[0]);
	}

	private Frame processTask(ScheduledTask task) {
		final FrameBuilder builder = new FrameBuilder().add("task").add(task.getClass().getSimpleName()).add("name", task.name$());
		List<Frame> jobFrames = new ArrayList<>();
		if (task.i$(CronTriggerTask.class)) {
			CronTriggerTask cron = task.a$(CronTriggerTask.class);
			FrameBuilder jobFrameBuilder = new FrameBuilder().add("job").add("Cron" + task.getClass().getSimpleName())
					.add("name", task.core$().id());
			jobFrameBuilder.add("cronTrigger")
					.add("pattern", cron.pattern())
					.add("mean", cron.mean());
			if (cron.timeZone() != null) jobFrameBuilder.add("timeZone", cron.timeZone());
			jobFrames.add(jobFrameBuilder.toFrame());
		}
		if (task.i$(BootTriggerTask.class)) {
			final FrameBuilder jobFrameBuilder = new FrameBuilder("onBootTrigger", "job", "Boot" + task.getClass().getSimpleName())
					.add("name", task.core$().id());
			jobFrames.add(jobFrameBuilder.toFrame());
		}
		builder.add("job", jobFrames.toArray(new Frame[0]));
		return builder.toFrame();
	}

	private Frame processDirectorySentinel(DirectorySentinelTask task) {
		final FrameBuilder builder = new FrameBuilder().add("task").add(task.getClass().getSimpleName())
				.add("event", task.events().stream().map(Enum::name).toArray(String[]::new))
				.add("file", task.directory() == null ? "" : task.directory())
				.add("name", task.name$())
				.add("package", packageName);
		return builder.toFrame();
	}

	private Template template() {
		return Formatters.customize(new SchedulerTemplate());
	}
}
