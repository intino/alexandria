package teseo.codegeneration.server.jmx;

import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;
import teseo.Operation;
import teseo.Parameter;
import teseo.codegeneration.action.MethodActionRenderer;
import teseo.jmx.JMXService;
import teseo.object.ObjectData;
import teseo.type.TypeData;

import java.io.File;
import java.util.List;

import static teseo.helpers.Commons.writeFrame;

public class JMXOperationsServiceRenderer {

	private final List<JMXService> services;
	private File src;
	private final File gen;
	private String packageName;

	public JMXOperationsServiceRenderer(Graph graph, File src, File gen, String packageName) {
		services = graph.find(JMXService.class);
		this.src = src;
		this.gen = gen;
		this.packageName = packageName;
	}

	public void execute() {
		this.services.forEach((service) -> {
			createInterface(service);
			createImplementation(service);
			createCorrespondingActions(service.operationList());
		});
	}

	private void createInterface(JMXService service) {
		Frame frame = new Frame().addTypes("jmx", "interface");
		frame.addSlot("name", service.name());
		frame.addSlot("package", packageName);
		for (Operation operation : service.operationList())
			frame.addSlot("operation", frameOf(operation));
		writeFrame(destinyPackage(), service.name() + "MBean", template().format(frame));
	}

	private void createImplementation(JMXService service) {
		Frame frame = new Frame().addTypes("jmx", "implementation");
		frame.addSlot("name", service.name());
		frame.addSlot("package", packageName);
		for (Operation operation : service.operationList())
			frame.addSlot("operation", frameOf(operation));
		writeFrame(destinyPackage(), service.name(), template().format(frame));
	}

	private void createCorrespondingActions(List<Operation> operations) {
		for (Operation operation : operations) new MethodActionRenderer(operation, src, packageName).execute();
	}

	private Frame frameOf(Operation operation) {
		final Frame frame = new Frame().addTypes("operation").addSlot("name", operation.name()).addSlot("action", operation.name()).
				addSlot("package", packageName).addSlot("returnType", operation.response() == null ? "void" : formatType(operation.response().asType()));
		setupParameters(operation.parameterList(), frame);
		return frame;
	}

	private String formatType(TypeData typeData) {
		return (typeData.is(ObjectData.class) ? (packageName + ".schemas.") : "") + typeData.type();
	}

	private void setupParameters(List<Parameter> parameters, Frame frame) {
		for (Parameter parameter : parameters)
			frame.addSlot("parameter", new Frame().addTypes("parameter").addSlot("name", parameter.name()).addSlot("type", formatType(parameter.asType())));
	}

	private Template template() {
		final Template template = JMXServerTemplate.create();
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}

	private File destinyPackage() {
		return new File(gen, "jmx");
	}
}
