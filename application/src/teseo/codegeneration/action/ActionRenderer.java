package teseo.codegeneration.action;

import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.Action;
import teseo.Schema;

import java.io.File;
import java.util.List;

import static teseo.helpers.Commons.javaFile;
import static teseo.helpers.Commons.writeFrame;

public class ActionRenderer {
	private final List<Action> applications;

	public ActionRenderer(Graph graph) {
		applications = graph.find(Action.class);
	}

	public void execute(File destiny, String packageName) {
		applications.forEach(action -> processAction(action, destiny, packageName));
	}

	private void processAction(Action action, File destiny, String packageName) {
		Frame frame = new Frame().addTypes("action");
		frame.addSlot("name", action.name());
		frame.addSlot("package", packageName);
		if (action.schema() != null) setupSchema(action.schema(), frame);
		frame.addSlot("returnType", action.response() == null ? "void" : action.response().asType().type());
		if (!alreadyRendered(destiny, action))
			writeFrame(destinyPackage(destiny), action.name() + "Action", template().format(frame));
	}

	private void setupSchema(Schema schema, Frame frame) {
		for (Schema.Element.Attribute attribute : schema.element().attributeList())
			frame.addSlot("parameter", new Frame().addTypes("parameter").addSlot("name", attribute.name()).addSlot("type", attribute.asType().type()));
	}

	private Template template() {
		final Template template = ActionTemplate.create();
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	private boolean alreadyRendered(File destiny, Action action) {
		return javaFile(destinyPackage(destiny), action.name() + "Action").exists();
	}

	private File destinyPackage(File destiny) {
		return new File(destiny, "actions");
	}

}
