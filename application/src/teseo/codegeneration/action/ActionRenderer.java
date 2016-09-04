package teseo.codegeneration.action;

import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.Action;
import teseo.object.ObjectData;
import teseo.type.TypeData;

import java.io.File;
import java.util.List;

import static teseo.helpers.Commons.javaFile;
import static teseo.helpers.Commons.writeFrame;

public class ActionRenderer {
	private final List<Action> applications;
	private String packageName;

	public ActionRenderer(Graph graph) {
		applications = graph.find(Action.class);
	}

	public void execute(File destiny, String packageName) {
		this.packageName = packageName;
		applications.forEach(action -> processAction(action, destiny));
	}

	private void processAction(Action action, File destiny) {
		Frame frame = new Frame().addTypes("action");
		frame.addSlot("name", action.name());
		frame.addSlot("package", packageName);
		setupParameters(action.parameterList(), frame);
		frame.addSlot("returnType", action.response() == null ? "void" : formatType(action.response().asType()));
		if (!alreadyRendered(destiny, action))
			writeFrame(destinyPackage(destiny), action.name() + "Action", template().format(frame));
	}


	private void setupParameters(List<Action.Parameter> parameters, Frame frame) {
		for (Action.Parameter parameter : parameters)
			frame.addSlot("parameter", new Frame().addTypes("parameter").addSlot("name", parameter.name()).addSlot("type", formatType(parameter.asType())));
	}

	private String formatType(TypeData typeData) {
		return (typeData.is(ObjectData.class) ? (packageName + ".schemas.") : "") + typeData.type();
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
