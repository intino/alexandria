package teseo.codegeneration.server.scheduling;

import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.Action;
import teseo.Schema;
import teseo.scheduled.ScheduledTrigger;

import java.io.File;
import java.util.List;

import static teseo.helpers.Commons.writeFrame;

public class ScheduledTriggerRenderer {
	private final List<ScheduledTrigger> triggers;
	private File genDestination;
	private String packageName;

	public ScheduledTriggerRenderer(Graph graph) {
		triggers = graph.find(ScheduledTrigger.class);
	}

	public void execute(File genDestination, String packageName) {
		this.genDestination = genDestination;
		this.packageName = packageName;
		this.triggers.forEach(this::processTrigger);
	}

	private void processTrigger(ScheduledTrigger trigger) {
		Frame frame = new Frame().addTypes("scheduled");
		frame.addSlot("name", trigger.name());
		frame.addSlot("package", packageName);
		for (Action action : trigger.actions()) {
			final Frame actionFrame = new Frame().addTypes("action").addSlot("name", action.name()).addSlot("package", packageName);
			if (action.schema() != null) setupSchema(action.schema(), frame);
			frame.addSlot("action", actionFrame);
		}
		writeFrame(destinyPackage(), trigger.name() + "Trigger", template().format(frame));
	}

	private void setupSchema(Schema schema, Frame frame) {
		for (Schema.Element.Attribute attribute : schema.element().attributeList())
			frame.addSlot("parameter", new Frame().addTypes("parameter").addSlot("name", attribute.name()).addSlot("type", attribute.asType().type()));
	}

	private Template template() {
		final Template template = ScheduledTriggerTemplate.create();
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	private File destinyPackage() {
		return new File(genDestination, "scheduling");
	}

}
