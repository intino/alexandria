package io.intino.pandora.plugin.codegeneration.server.jmx;

import com.intellij.openapi.project.Project;
import io.intino.pandora.plugin.Parameter;
import io.intino.pandora.plugin.codegeneration.action.JMXActionRenderer;
import io.intino.pandora.plugin.helpers.Commons;
import io.intino.pandora.plugin.jmx.JMXService;
import io.intino.pandora.plugin.jmx.JMXService.Operation;
import io.intino.pandora.plugin.object.ObjectData;
import io.intino.pandora.plugin.type.TypeData;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

public class JMXOperationsServiceRenderer {

	private final Project project;
	private final List<JMXService> services;
	private File src;
	private final File gen;
	private String packageName;
	private final String boxName;

	public JMXOperationsServiceRenderer(Project project, Graph graph, File src, File gen, String packageName, String boxName) {
		this.project = project;
		this.services = graph.find(JMXService.class);
		this.src = src;
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
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
		frame.addSlot("box", boxName);
		for (Operation operation : service.operationList())
			frame.addSlot("operation", frameOf(operation));
		Commons.writeFrame(destinyPackage(), service.name() + "MBean", template().format(frame));
	}

	private void createImplementation(JMXService service) {
		Frame frame = new Frame().addTypes("jmx", "implementation");
		frame.addSlot("name", service.name());
		frame.addSlot("package", packageName);
		for (Operation operation : service.operationList())
			frame.addSlot("operation", frameOf(operation));
		Commons.writeFrame(destinyPackage(), service.name(), template().format(frame));
	}

	private void createCorrespondingActions(List<Operation> operations) {
		for (Operation operation : operations)
			new JMXActionRenderer(project, operation, src, packageName, boxName).execute();
	}

	private Frame frameOf(Operation operation) {
		final Frame frame = new Frame().addTypes("operation").addSlot("name", operation.name()).addSlot("action", operation.name()).
				addSlot("package", packageName).addSlot("returnType", operation.response() == null ? "void" : formatType(operation.response().asType()));
		setupParameters(operation.parameterList(), frame);
		return frame;
	}

	private String formatType(TypeData typeData) {
		return (typeData.is(ObjectData.class) ? (packageName + ".structures.") : "") + typeData.type();
	}

	private void setupParameters(List<Parameter> parameters, Frame frame) {
		for (Parameter parameter : parameters)
			frame.addSlot("parameter", new Frame().addTypes("parameter").addSlot("name", parameter.name()).addSlot("type", formatType(parameter.asType())));
	}

	private Template template() {
		final Template template = JMXServerTemplate.create();
		template.add("ValidPackage", Commons::validPackage);
		return template;
	}

	private File destinyPackage() {
		return new File(gen, "jmx");
	}
}
