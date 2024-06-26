package io.intino.konos.builder.codegeneration.accessor.jmx;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.schema.SchemaListRenderer;
import io.intino.konos.builder.codegeneration.services.jmx.JMXServerTemplate;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.dsl.Data;
import io.intino.konos.dsl.Parameter;
import io.intino.konos.dsl.Service;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.all;

public class JMXAccessorRenderer extends Renderer {
	private final Service.JMX service;
	private final File destination;
	private final String packageName;

	public JMXAccessorRenderer(CompilationContext compilationContext, Service.JMX restService, File destination) {
		super(compilationContext);
		this.service = restService;
		this.destination = destination;
		this.destination.mkdirs();
		this.packageName = compilationContext.packageName() + ".box";
	}

	@Override
	public void render() throws KonosException {
		new SchemaListRenderer(context, service.graph(), destination, packageName).execute();
		createInterface(service);
		createService(service);
	}

	private void createInterface(Service.JMX service) {
		FrameBuilder frame = new FrameBuilder("jmx", "interface");
		fillFrame(service, frame);
		Commons.writeFrame(destinationPackage(), service.name$() + "MBean", new JMXServerTemplate().render(frame, all));
	}

	private File destinationPackage() {
		return new File(destination, "jmx");
	}

	private void createService(Service.JMX service) {
		FrameBuilder builder = new FrameBuilder("accessor");
		fillFrame(service, builder);
		Commons.writeFrame(destination, snakeCaseToCamelCase(service.name$()) + "JMXAccessor", new JMXAccessorTemplate().render(builder.toFrame(), all));
	}

	private void fillFrame(Service.JMX service, FrameBuilder builder) {
		builder.add("name", service.name$());
		builder.add("package", packageName);
		if (!service.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName));
		for (Service.JMX.Operation operation : service.operationList())
			builder.add("operation", frameOf(operation));
	}

	private Frame frameOf(Service.JMX.Operation operation) {
		final FrameBuilder builder = new FrameBuilder("operation").add("name", operation.name$()).add("action", operation.name$()).
				add("package", packageName).add("returnType", operation.response() == null ? "void" : formatType(operation.response().asType()));
		setupParameters(operation.parameterList(), builder);
		return builder.toFrame();
	}

	private String formatType(Data.Type typeData) {
		return (typeData.i$(Data.Object.class) ? (packageName + ".schemas.") : "") + typeData.type();
	}

	private void setupParameters(List<Parameter> parameters, FrameBuilder builder) {
		for (Parameter parameter : parameters)
			builder.add("parameter", new FrameBuilder("parameter").add("name", parameter.name$()).add("type", formatType(parameter.asType())).toFrame());
	}

}
