package teseo.codegeneration.server.scheduling;

import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.codegeneration.action.ActionTemplate;
import teseo.scheduled.ScheduledTrigger;

import java.io.File;
import java.util.List;

import static teseo.helpers.Commons.javaFile;
import static teseo.helpers.Commons.writeFrame;

public class ScheduledTriggerRenderer {
	private final List<ScheduledTrigger> triggers;
	private File srcDestination;
	private File genDestination;
	private String packageName;

	public ScheduledTriggerRenderer(Graph graph, File src, File gen, String packageName) {
		triggers = graph.find(ScheduledTrigger.class);
		this.srcDestination = src;
		this.genDestination = gen;
		this.packageName = packageName;
	}

	public void execute() {
		this.triggers.forEach(this::processTrigger);
	}

	private void processTrigger(ScheduledTrigger trigger) {
		Frame frame = new Frame().addTypes("scheduled");
		frame.addSlot("name", trigger.name());
		frame.addSlot("package", packageName);
		writeFrame(destinyPackage(), trigger.name() + "Trigger", template().format(frame));
		createCorrespondingAction(trigger);
	}

	private void createCorrespondingAction(ScheduledTrigger trigger) {
		Frame frame = new Frame().addTypes("action");
		frame.addSlot("name", trigger.name());
		frame.addSlot("package", packageName);
		if (!alreadyRendered(srcDestination, trigger))
			writeFrame(actionsPackage(srcDestination), trigger.name() + "Action", actionTemplate().format(frame));
	}

	private Template actionTemplate() {
		final Template template = ActionTemplate.create();
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	private Template template() {
		final Template template = ScheduledTriggerTemplate.create();
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	private boolean alreadyRendered(File destiny, ScheduledTrigger trigger) {
		return javaFile(actionsPackage(destiny), trigger.name() + "Action").exists();
	}

	private File actionsPackage(File destiny) {
		return new File(destiny, "actions");
	}


	private File destinyPackage() {
		return new File(genDestination, "scheduling");
	}

}
