package io.intino.konos.builder.codegeneration.services.jmx;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.action.JMXActionRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Data;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Parameter;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.Service.JMX.Operation;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static io.intino.konos.builder.helpers.Commons.javaFile;

public class JMXOperationsServiceRenderer extends Renderer {
	private final List<Service.JMX> services;

	public JMXOperationsServiceRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext, Target.Owner);
		this.services = graph.serviceList(Service::isJMX).map(Service::asJMX).collect(Collectors.toList());
	}

	@Override
	public void render() {
		this.services.forEach((service) -> {
			createInterface(service);
			createImplementation(service);
			createCorrespondingActions(service.operationList());
		});
	}

	private void createInterface(Service.JMX service) {
		FrameBuilder builder = new FrameBuilder("jmx", "interface")
				.add("name", service.name$())
				.add("package", packageName())
				.add("box", boxName());
		if (!service.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName()));
		for (Operation operation : service.operationList())
			builder.add("operation", frameOf(operation));
		Commons.writeFrame(genPackage(), service.name$() + "MBean", template().render(builder));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(service), javaFile(genPackage(), service.name$() + "MBean").getAbsolutePath()));

	}

	private void createImplementation(Service.JMX service) {
		FrameBuilder builder = new FrameBuilder("jmx", "implementation")
				.add("name", service.name$())
				.add("box", boxName())
				.add("package", packageName())
				.add("operation", service.operationList().stream().map(this::frameOf).toArray(Frame[]::new));
		Commons.writeFrame(genPackage(), service.name$(), template().render(builder));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(service), javaFile(genPackage(), service.name$()).getAbsolutePath()));
	}

	private void createCorrespondingActions(List<Operation> operations) {
		for (Operation operation : operations)
			new JMXActionRenderer(context, operation).execute();
	}

	private Frame frameOf(Operation operation) {
		final FrameBuilder builder = new FrameBuilder("operation").add("name", operation.name$()).add("action", operation.name$()).
				add("package", packageName()).add("returnType", returnType(operation));
		builder.add("description", operation.description());
		setupParameters(operation.parameterList(), builder);
		return builder.toFrame();
	}

	private Frame returnType(Operation operation) {
		final FrameBuilder builder = new FrameBuilder("returnType").add("value", operation.response() == null ? "void" : formatType(operation.response().asType()));
		if (operation.response() != null && operation.response().i$(Data.List.class)) builder.add("list");
		return builder.toFrame();
	}

	private String formatType(Data.Type typeData) {
		return (typeData.i$(Data.Object.class) ? (packageName() + ".schemas.") : "") + typeData.type();
	}

	private void setupParameters(List<Parameter> parameters, FrameBuilder builder) {
		for (Parameter parameter : parameters) {
			final FrameBuilder parameterBuilder = new FrameBuilder("parameter").add("name", parameter.name$()).add("type", formatType(parameter.asType()));
			if (parameter.i$(Data.List.class)) parameterBuilder.add("list");
			builder.add("parameter", parameterBuilder.toFrame());
		}
	}

	private Template template() {
		return Formatters.customize(new JMXServerTemplate());
	}

	private File genPackage() {
		return new File(gen(), "jmx");
	}
}