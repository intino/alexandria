package io.intino.konos.builder.codegeneration.services.jmx;

import com.intellij.openapi.project.Project;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
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

import java.io.File;
import java.util.List;
import java.util.Map;

public class JMXOperationsServiceRenderer {

	private final Project project;
	private final List<JMXService> services;
	private final File gen;
	private final String boxName;
	private final Map<String, String> classes;
	private File src;
	private String packageName;

	public JMXOperationsServiceRenderer(Project project, KonosGraph graph, File src, File gen, String packageName, String boxName, Map<String, String> classes) {
		this.project = project;
		this.services = graph.jMXServiceList();
		this.src = src;
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
		this.classes = classes;
	}

	public void execute() {
		this.services.forEach((service) -> {
			createInterface(service);
			createImplementation(service);
			createCorrespondingActions(service.operationList());
		});
	}

	private void createInterface(JMXService service) {
		FrameBuilder builder = new FrameBuilder("jmx", "interface")
				.add("name", service.name$())
				.add("package", packageName)
				.add("box", boxName);
		if (!service.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName));
		for (Operation operation : service.operationList())
			builder.add("operation", frameOf(operation));
		Commons.writeFrame(destinationPackage(), service.name$() + "MBean", template().render(builder));
	}

	private void createImplementation(JMXService service) {
		FrameBuilder builder = new FrameBuilder("jmx", "implementation")
				.add("name", service.name$())
				.add("box", boxName)
				.add("package", packageName)
				.add("operation", service.operationList().stream().map(this::frameOf).toArray(Frame[]::new));
		Commons.writeFrame(destinationPackage(), service.name$(), template().render(builder));
	}

	private void createCorrespondingActions(List<Operation> operations) {
		for (Operation operation : operations)
			new JMXActionRenderer(project, operation, src, packageName, boxName, classes).execute();
	}

	private Frame frameOf(Operation operation) {
		final FrameBuilder builder = new FrameBuilder("operation").add("name", operation.name$()).add("action", operation.name$()).
				add("package", packageName).add("returnType", returnType(operation));
		builder.add("description", operation.description());
		setupParameters(operation.parameterList(), builder);
		return builder.toFrame();
	}

	private Frame returnType(Operation operation) {
		final FrameBuilder builder = new FrameBuilder("returnType").add("value", operation.response() == null ? "void" : formatType(operation.response().asType()));
		if (operation.response() != null && operation.response().i$(ListData.class)) builder.add("list");
		return builder.toFrame();
	}

	private String formatType(TypeData typeData) {
		return (typeData.i$(ObjectData.class) ? (packageName + ".schemas.") : "") + typeData.type();
	}

	private void setupParameters(List<Parameter> parameters, FrameBuilder builder) {
		for (Parameter parameter : parameters) {
			final FrameBuilder parameterBuilder = new FrameBuilder("parameter").add("name", parameter.name$()).add("type", formatType(parameter.asType()));
			if (parameter.i$(ListData.class)) parameterBuilder.add("list");
			builder.add("parameter", parameterBuilder.toFrame());
		}
	}

	private Template template() {
		return Formatters.customize(new JMXServerTemplate());
	}

	private File destinationPackage() {
		return new File(gen, "jmx");
	}
}