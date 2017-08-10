package io.intino.konos.builder.codegeneration.task;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.action.ActionTemplate;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Task;
import io.intino.konos.model.graph.directorysentinel.DirectorySentinelTask;
import io.intino.konos.model.graph.scheduled.ScheduledTask;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskRenderer {
	private final List<ScheduledTask> scheduledTasks;
	private final List<Task> tasks;
	private File srcDestination;
	private File genDestination;
	private String packageName;
	private final String boxName;

	public TaskRenderer(Project project, KonosGraph graph, File src, File gen, String packageName, String boxName) {
		this.scheduledTasks = graph.scheduledTaskList();
		this.tasks = graph.taskList().stream().filter(t -> !t.isScheduled()).collect(Collectors.toList());
		this.srcDestination = src;
		this.genDestination = gen;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		this.scheduledTasks.forEach(this::processTrigger);
		this.tasks.stream().filter(Task::isDirectorySentinel).forEach(this::processDirectorySentinel);
	}

	private void processDirectorySentinel(Task task) {
		Frame frame = new Frame().addTypes("action");
		frame.addSlot("name", task.name$());
		frame.addSlot("box", boxName);
		frame.addSlot("package", packageName);
		frame.addSlot("parameter", (AbstractFrame[]) parameters(task.asDirectorySentinel()));
		if (!alreadyRendered(srcDestination, task))
			Commons.writeFrame(actionsPackage(srcDestination), task.name$() + "Action", actionTemplate().format(frame));
	}

	private Frame[] parameters(DirectorySentinelTask task) {
		List<Frame> list = new ArrayList<>();
		list.add(new Frame().addTypes("parameter").addSlot("type", URL.class.getCanonicalName()).addSlot("name", "directory"));
		list.add(new Frame().addTypes("parameter").addSlot("type", "io.intino.konos.scheduling.directory.KonosDirectorySentinel.Event").addSlot("name", "event"));
		return list.toArray(new Frame[list.size()]);
	}

	private List<DirectorySentinelTask.Events> directoryEvents(DirectorySentinelTask task) {
		return task.events();
	}

	private void processTrigger(ScheduledTask task) {
		Frame frame = new Frame().addTypes("scheduled");
		frame.addSlot("name", task.name$());
		frame.addSlot("box", boxName);
		frame.addSlot("package", packageName);
		Commons.writeFrame(destinyPackage(), task.name$() + "Task", template().format(frame));
		createCorrespondingAction(task);
	}

	private void createCorrespondingAction(ScheduledTask task) {
		Frame frame = new Frame().addTypes("action");
		frame.addSlot("name", task.name$());
		frame.addSlot("box", boxName);
		frame.addSlot("package", packageName);
		if (!alreadyRendered(srcDestination, task.a$(Task.class)))
			Commons.writeFrame(actionsPackage(srcDestination), task.name$() + "Action", actionTemplate().format(frame));
	}

	private Template actionTemplate() {
		return Formatters.customize(ActionTemplate.create());
	}

	private Template template() {
		return Formatters.customize(TaskTemplate.create());
	}

	private boolean alreadyRendered(File destiny, Task task) {
		return Commons.javaFile(actionsPackage(destiny), task.name$() + "Action").exists();
	}

	private File actionsPackage(File destiny) {
		return new File(destiny, "actions");
	}


	private File destinyPackage() {
		return new File(genDestination, "scheduling");
	}

}
