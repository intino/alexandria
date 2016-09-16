package teseo.codegeneration.action;

import org.siani.itrules.model.Frame;
import teseo.Method;
import teseo.Parameter;

import java.io.File;
import java.util.List;

import static teseo.helpers.Commons.writeFrame;

public class MethodActionRenderer extends ActionRenderer {
	private final Method method;

	public MethodActionRenderer(Method method, File destiny, String packageName) {
		super(destiny, packageName);
		this.method = method;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("action");
		frame.addSlot("name", method.name());
		frame.addSlot("package", packageName);
		setupParameters(method.parameterList(), frame);
		frame.addSlot("returnType", method.response() == null || method.response().asType() == null ? "void" : formatType(method.response().asType()))
		;
		if (!alreadyRendered(destiny, method.name()))
			writeFrame(destinyPackage(destiny), method.name() + "Action", template().format(frame));
	}


	private void setupParameters(List<Parameter> parameters, Frame frame) {
		for (Parameter parameter : parameters)
			frame.addSlot("parameter", new Frame().addTypes("parameter").addSlot("name", parameter.name()).addSlot("type", formatType(parameter.asType())));
	}

}
