package io.intino.pandora.plugin.codegeneration.server.jmx;

import com.intellij.openapi.project.Project;
import io.intino.pandora.model.Parameter;
import io.intino.pandora.model.Schema;
import io.intino.pandora.model.jmx.JMXService;
import io.intino.pandora.model.jmx.JMXService.Operation;
import io.intino.pandora.model.list.ListData;
import io.intino.pandora.model.object.ObjectData;
import io.intino.pandora.model.type.TypeData;
import io.intino.pandora.plugin.codegeneration.Formatters;
import io.intino.pandora.plugin.codegeneration.action.JMXActionRenderer;
import io.intino.pandora.plugin.helpers.Commons;
import io.intino.tara.magritte.Graph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

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
		if (!service.graph().find(Schema.class).isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		for (Operation operation : service.operationList())
			frame.addSlot("operation", frameOf(operation));
		Commons.writeFrame(destinationPackage(), service.name() + "MBean", template().format(frame));
	}

	private void createImplementation(JMXService service) {
		Frame frame = new Frame().addTypes("jmx", "implementation");
		frame.addSlot("name", service.name());
		frame.addSlot("box", boxName);
		frame.addSlot("package", packageName);
		for (Operation operation : service.operationList())
			frame.addSlot("operation", frameOf(operation));
		Commons.writeFrame(destinationPackage(), service.name(), template().format(frame));
	}

	private void createCorrespondingActions(List<Operation> operations) {
		for (Operation operation : operations)
			new JMXActionRenderer(project, operation, src, packageName, boxName).execute();
	}

	private Frame frameOf(Operation operation) {
		final Frame frame = new Frame().addTypes("operation").addSlot("name", operation.name()).addSlot("action", operation.name()).
				addSlot("package", packageName).addSlot("returnType", returnType(operation));
		setupParameters(operation.parameterList(), frame);
		return frame;
	}

	private Frame returnType(Operation operation) {
		final Frame frame = new Frame().addTypes("returnType").addSlot("value", operation.response() == null ? "void" : formatType(operation.response().asType()));
		if (operation.response().is(ListData.class)) frame.addTypes("list");
		return frame;
	}

	private String formatType(TypeData typeData) {
		return (typeData.is(ObjectData.class) ? (packageName + ".schemas.") : "") + typeData.type();
	}

	private void setupParameters(List<Parameter> parameters, Frame frame) {
		for (Parameter parameter : parameters) {
			final Frame parameterFrame = new Frame().addTypes("parameter").addSlot("name", parameter.name()).addSlot("type", formatType(parameter.asType()));
			if (parameter.is(ListData.class)) parameterFrame.addTypes("list");
			frame.addSlot("parameter", parameterFrame);
		}
	}

	private Template template() {
		return Formatters.customize(JMXServerTemplate.create());
	}

	private File destinationPackage() {
		return new File(gen, "jmx");
	}
}
