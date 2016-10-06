package io.intino.pandora.plugin.codegeneration.server.scheduling;

import io.intino.pandora.plugin.codegeneration.action.ActionTemplate;
import io.intino.pandora.plugin.helpers.Commons;
import io.intino.pandora.plugin.scheduled.ScheduledTask;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

public class ScheduledTaskRenderer {
    private final List<ScheduledTask> tasks;
    private File srcDestination;
    private File genDestination;
    private String packageName;

    public ScheduledTaskRenderer(Graph graph, File src, File gen, String packageName) {
        tasks = graph.find(ScheduledTask.class);
        this.srcDestination = src;
        this.genDestination = gen;
        this.packageName = packageName;
    }

    public void execute() {
        this.tasks.forEach(this::processTrigger);
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
        if (!alreadyRendered(srcDestination, task))
            Commons.writeFrame(actionsPackage(srcDestination), task.name() + "Action", actionTemplate().format(frame));
    }

    private Template actionTemplate() {
        final Template template = ActionTemplate.create();
        template.add("ValidPackage", Commons::validPackage);
        return template;
    }

    private Template template() {
        final Template template = ScheduledTaskTemplate.create();
        template.add("ValidPackage", Commons::validPackage);
        return template;
    }

    private boolean alreadyRendered(File destiny, ScheduledTask task) {
        return Commons.javaFile(actionsPackage(destiny), task.name() + "Action").exists();
    }

    private File actionsPackage(File destiny) {
        return new File(destiny, "actions");
    }


    private File destinyPackage() {
        return new File(genDestination, "scheduling");
    }

}
