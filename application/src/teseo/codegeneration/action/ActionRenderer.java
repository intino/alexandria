package teseo.codegeneration.action;

import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import teseo.Method;
import teseo.Parameter;
import teseo.object.ObjectData;
import teseo.type.TypeData;

import java.io.File;
import java.util.List;

import static teseo.helpers.Commons.javaFile;
import static teseo.helpers.Commons.writeFrame;

public class ActionRenderer {
	private final Method method;
	private String packageName;

	public ActionRenderer(Method method) {
		this.method = method;
	}

	public void execute(File destiny, String packageName) {
		this.packageName = packageName;
		processMethod(destiny);
	}

	private void processMethod(File destiny) {
		Frame frame = new Frame().addTypes("action");
		frame.addSlot("name", method.name());
		frame.addSlot("package", packageName);
		setupParameters(method.parameterList(), frame);
		frame.addSlot("returnType", method.response() == null || method.response().asType() == null ? "void" : formatType(method.response().asType()))
		;
		if (!alreadyRendered(destiny, method))
			writeFrame(destinyPackage(destiny), method.name() + "Action", template().format(frame));
	}


	private void setupParameters(List<Parameter> parameters, Frame frame) {
		for (Parameter parameter : parameters)
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

	private boolean alreadyRendered(File destiny, Method method) {
		return javaFile(destinyPackage(destiny), method.name() + "Action").exists();
	}

	private File destinyPackage(File destiny) {
		return new File(destiny, "actions");
	}

}
