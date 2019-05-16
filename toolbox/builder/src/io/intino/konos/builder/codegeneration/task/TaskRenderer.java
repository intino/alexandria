package io.intino.konos.builder.codegeneration.task;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.action.ActionTemplate;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Task;
import io.intino.konos.model.graph.directorysentinel.DirectorySentinelTask;
import io.intino.konos.model.graph.scheduled.ScheduledTask;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class TaskRenderer extends Renderer {
	private final List<ScheduledTask> scheduledTasks;
	private final List<Task> tasks;

	public TaskRenderer(Settings settings, KonosGraph graph) {
		super(settings, Target.Service);
		this.scheduledTasks = graph.scheduledTaskList();
		this.tasks = graph.taskList().stream().filter(t -> !t.isScheduled()).collect(Collectors.toList());
	}

	@Override
	public void render() {
		this.scheduledTasks.forEach(this::processTrigger);
		this.tasks.stream().filter(Task::isDirectorySentinel).forEach(this::processDirectorySentinel);
	}

	private void processDirectorySentinel(Task task) {
		FrameBuilder frame = new FrameBuilder("action")
				.add("name", task.name$())
				.add("box", boxName())
				.add("package", packageName())
				.add("parameter", parameters(task.asDirectorySentinel()));
		if (!alreadyRendered(src(), task))
			writeFrame(actionsPackage(src()), task.name$() + "Action", actionTemplate().render(frame));
	}

	private Frame[] parameters(DirectorySentinelTask task) {
		List<Frame> list = new ArrayList<>();
		list.add(new FrameBuilder("parameter").add("type", URL.class.getCanonicalName()).add("name", "directory").toFrame());
		list.add(new FrameBuilder("parameter").add("type", "io.intino.konos.scheduling.directory.KonosDirectorySentinel.Event").add("name", "event").toFrame());
		return list.toArray(new Frame[0]);
	}

	private void processTrigger(ScheduledTask task) {
		writeFrame(destinyPackage(), task.name$() + "Task", template().render(new FrameBuilder("scheduled")
				.add("name", task.name$())
				.add("box", boxName())
				.add("package", packageName()).toFrame()));
		createCorrespondingAction(task);
	}

	private void createCorrespondingAction(ScheduledTask task) {
		if (!alreadyRendered(src(), task.a$(Task.class)))
			writeFrame(actionsPackage(src()), task.name$() + "Action", actionTemplate().
					render(new FrameBuilder("action")
							.add("name", task.name$())
							.add("box", boxName())
							.add("package", packageName()).toFrame()));
	}

	private Template actionTemplate() {
		return Formatters.customize(new ActionTemplate());
	}

	private Template template() {
		return Formatters.customize(new TaskTemplate());
	}

	private boolean alreadyRendered(File destiny, Task task) {
		return Commons.javaFile(actionsPackage(destiny), task.name$() + "Action").exists();
	}

	private File actionsPackage(File destiny) {
		return new File(destiny, "actions");
	}

	private File destinyPackage() {
		return new File(gen(), "scheduling");
	}
}