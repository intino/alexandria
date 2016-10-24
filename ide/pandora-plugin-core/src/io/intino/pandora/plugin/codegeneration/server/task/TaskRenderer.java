package io.intino.pandora.plugin.codegeneration.server.task;

import com.intellij.openapi.project.Project;
import io.intino.pandora.plugin.Task;
import io.intino.pandora.plugin.codegeneration.action.ActionTemplate;
import io.intino.pandora.plugin.directorysentinel.DirectorySentinelTask;
import io.intino.pandora.plugin.helpers.Commons;
import io.intino.pandora.plugin.scheduled.ScheduledTask;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

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

	public TaskRenderer(Project project, Graph graph, File src, File gen, String packageName) {
		scheduledTasks = graph.find(ScheduledTask.class);
		tasks = graph.find(Task.class).stream().filter(t -> !t.isScheduled()).collect(Collectors.toList());
		this.srcDestination = src;
		this.genDestination = gen;
		this.packageName = packageName;
	}

	public void execute() {
		this.scheduledTasks.forEach(this::processTrigger);
		this.tasks.stream().filter(Task::isDirectorySentinel).forEach(this::processDirectorySentinel);
	}

	private void processDirectorySentinel(Task task) {
		Frame frame = new Frame().addTypes("action");
		frame.addSlot("name", task.name());
		frame.addSlot("package", packageName);
		frame.addSlot("parameter", (AbstractFrame[]) parameters(task.asDirectorySentinel()));
		if (!alreadyRendered(srcDestination, task))
			Commons.writeFrame(actionsPackage(srcDestination), task.name() + "Action", actionTemplate().format(frame));
	}

	private Frame[] parameters(DirectorySentinelTask task) {
		List<Frame> list = new ArrayList<>();
		list.add(new Frame().addTypes("parameter").addSlot("type", URL.class.getCanonicalName()).addSlot("name", "directory"));
		list.add(new Frame().addTypes("parameter").addSlot("type", "io.intino.pandora.scheduling.directory.PandoraDirectorySentinel.Event").addSlot("name", "event"));
		return list.toArray(new Frame[list.size()]);
	}

	private List<DirectorySentinelTask.Events> directoryEvents(DirectorySentinelTask task) {
		return task.events();
	}

	private void processTrigger(ScheduledTask task) {
		Frame frame = new Frame().addTypes("scheduled");
		frame.addSlot("name", task.name());
		frame.addSlot("package", packageName);
		Commons.writeFrame(destinyPackage(), task.name() + "Task", template().format(frame));
		createCorrespondingAction(task);
	}

	private void createCorrespondingAction(ScheduledTask task) {
		Frame frame = new Frame().addTypes("action");
		frame.addSlot("name", task.name());
		frame.addSlot("package", packageName);
		if (!alreadyRendered(srcDestination, task.as(Task.class)))
			Commons.writeFrame(actionsPackage(srcDestination), task.name() + "Action", actionTemplate().format(frame));
	}

	private Template actionTemplate() {
		final Template template = ActionTemplate.create();
		template.add("ValidPackage", Commons::validPackage);
		return template;
	}

	private Template template() {
		final Template template = TaskTemplate.create();
		template.add("ValidPackage", Commons::validPackage);
		return template;
	}

	private boolean alreadyRendered(File destiny, Task task) {
		return Commons.javaFile(actionsPackage(destiny), task.name() + "Action").exists();
	}

	private File actionsPackage(File destiny) {
		return new File(destiny, "actions");
	}


	private File destinyPackage() {
		return new File(genDestination, "scheduling");
	}

}
