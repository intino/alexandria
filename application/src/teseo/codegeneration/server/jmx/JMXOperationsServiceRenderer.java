package teseo.codegeneration.server.jmx;

import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.jmx.JMXService;
import teseo.jmx.JMXService.Operation;
import teseo.object.ObjectData;
import teseo.type.TypeData;

import java.io.File;
import java.util.List;

import static teseo.helpers.Commons.writeFrame;

public class JMXOperationsServiceRenderer {

	private final List<JMXService> services;
	private File genDestination;
	private String packageName;

	public JMXOperationsServiceRenderer(Graph graph) {
		services = graph.find(JMXService.class);
	}

	public void execute(File genDestination, String packageName) {
		this.genDestination = genDestination;
		this.packageName = packageName;
		this.services.forEach(this::writeInterface);
		this.services.forEach(this::writeImplementation);
	}

	private void writeInterface(JMXService service) {
		Frame frame = new Frame().addTypes("jmx", "interface");
		frame.addSlot("name", service.name());
		frame.addSlot("package", packageName);
		for (Operation operation : service.operationList())
			frame.addSlot("operation", frameOf(operation));
		writeFrame(destinyPackage(), service.name() + "MBean", template().format(frame));
	}

	private void writeImplementation(JMXService service) {
		Frame frame = new Frame().addTypes("jmx", "implementation");
		frame.addSlot("name", service.name());
		frame.addSlot("package", packageName);
		for (Operation operation : service.operationList())
			frame.addSlot("operation", frameOf(operation));
		writeFrame(destinyPackage(), service.name(), template().format(frame));
	}

	private Frame frameOf(Operation operation) {
		final Frame frame = new Frame().addTypes("operation").addSlot("name", operation.name()).addSlot("action", action.name()).
				addSlot("package", packageName).addSlot("returnType", operation.response() == null ? "void" : formatType(action.response().asType()));
		setupParameters(action.parameterList(), frame);
		return frame;
	}

	private String formatType(TypeData typeData) {
		return (typeData.is(ObjectData.class) ? (packageName + ".schemas.") : "") + typeData.type();
	}

	private void setupParameters(List<Operation.Parameter> parameters, Frame frame) {
		for (Action.Parameter attribute : parameters)
			frame.addSlot("parameter", new Frame().addTypes("parameter").addSlot("name", attribute.name()).addSlot("type", formatType(attribute.asType())));
	}

	private Template template() {
		final Template template = JMXServerTemplate.create();
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	private File destinyPackage() {
		return new File(genDestination, "jmx");
	}
}
