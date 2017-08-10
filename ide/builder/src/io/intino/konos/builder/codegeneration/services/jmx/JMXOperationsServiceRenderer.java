package io.intino.konos.builder.codegeneration.services.jmx;

import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.action.JMXActionRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Parameter;
import io.intino.konos.model.graph.jmx.JMXService;
import io.intino.konos.model.graph.jmx.JMXService.Operation;
import io.intino.konos.model.graph.list.ListData;
import io.intino.konos.model.graph.object.ObjectData;
import io.intino.konos.model.graph.type.TypeData;
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

	public JMXOperationsServiceRenderer(Project project, KonosGraph graph, File src, File gen, String packageName, String boxName) {
		this.project = project;
		this.services = graph.jMXServiceList();
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
		frame.addSlot("name", service.name$());
		frame.addSlot("package", packageName);
		frame.addSlot("box", boxName);
		if (!service.graph().schemaList().isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		for (Operation operation : service.operationList())
			frame.addSlot("operation", frameOf(operation));
		Commons.writeFrame(destinationPackage(), service.name$() + "MBean", template().format(frame));
	}

	private void createImplementation(JMXService service) {
		Frame frame = new Frame().addTypes("jmx", "implementation");
		frame.addSlot("name", service.name$());
		frame.addSlot("box", boxName);
		frame.addSlot("package", packageName);
		for (Operation operation : service.operationList())
			frame.addSlot("operation", frameOf(operation));
		Commons.writeFrame(destinationPackage(), service.name$(), template().format(frame));
	}

	private void createCorrespondingActions(List<Operation> operations) {
		for (Operation operation : operations)
			new JMXActionRenderer(project, operation, src, packageName, boxName).execute();
	}

	private Frame frameOf(Operation operation) {
		final Frame frame = new Frame().addTypes("operation").addSlot("name", operation.name$()).addSlot("action", operation.name$()).
				addSlot("package", packageName).addSlot("returnType", returnType(operation));
		frame.addSlot("description", operation.description());
		setupParameters(operation.parameterList(), frame);
		return frame;
	}

	private Frame returnType(Operation operation) {
		final Frame frame = new Frame().addTypes("returnType").addSlot("value", operation.response() == null ? "void" : formatType(operation.response().asType()));
		if (operation.response() != null && operation.response().i$(ListData.class)) frame.addTypes("list");
		return frame;
	}

	private String formatType(TypeData typeData) {
		return (typeData.i$(ObjectData.class) ? (packageName + ".schemas.") : "") + typeData.type();
	}

	private void setupParameters(List<Parameter> parameters, Frame frame) {
		for (Parameter parameter : parameters) {
			final Frame parameterFrame = new Frame().addTypes("parameter").addSlot("name", parameter.name$()).addSlot("type", formatType(parameter.asType()));
			if (parameter.i$(ListData.class)) parameterFrame.addTypes("list");
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
