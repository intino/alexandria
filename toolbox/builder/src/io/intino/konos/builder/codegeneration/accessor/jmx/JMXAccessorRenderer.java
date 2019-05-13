package io.intino.konos.builder.codegeneration.accessor.jmx;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.schema.SchemaRenderer;
import io.intino.konos.builder.codegeneration.services.jmx.JMXServerTemplate;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Parameter;
import io.intino.konos.model.graph.jmx.JMXService;
import io.intino.konos.model.graph.object.ObjectData;
import io.intino.konos.model.graph.type.TypeData;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class JMXAccessorRenderer {
	private final JMXService service;
	private File destination;
	private String packageName;

	public JMXAccessorRenderer(JMXService restService, File destination, String packageName) {
		this.service = restService;
		this.destination = new File(destination, "konos");
		this.packageName = packageName + ".konos";
	}

	public void execute() {
		new SchemaRenderer(service.graph(), destination, packageName, new HashMap<>()).execute();
		createInterface(service);
		createService(service);
	}

	private void createInterface(JMXService service) {
		FrameBuilder frame = new FrameBuilder("jmx", "interface");
		fillFrame(service, frame);
		Commons.writeFrame(destinationPackage(), service.name$() + "MBean", interfaceTemplate().render(frame));
	}

	private File destinationPackage() {
		return new File(destination, "jmx");
	}

	private void createService(JMXService service) {
		FrameBuilder builder = new FrameBuilder("accessor");
		fillFrame(service, builder);
		Commons.writeFrame(destination, snakeCaseToCamelCase(service.name$()) + "JMXAccessor", template().render(builder.toFrame()));
	}

	private void fillFrame(JMXService service, FrameBuilder builder) {
		builder.add("name", service.name$());
		builder.add("package", packageName);
		if (!service.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName));
		for (JMXService.Operation operation : service.operationList())
			builder.add("operation", frameOf(operation));
	}

	private Frame frameOf(JMXService.Operation operation) {
		final FrameBuilder builder = new FrameBuilder("operation").add("name", operation.name$()).add("action", operation.name$()).
				add("package", packageName).add("returnType", operation.response() == null ? "void" : formatType(operation.response().asType()));
		setupParameters(operation.parameterList(), builder);
		return builder.toFrame();
	}

	private String formatType(TypeData typeData) {
		return (typeData.i$(ObjectData.class) ? (packageName + ".schemas.") : "") + typeData.type();
	}

	private void setupParameters(List<Parameter> parameters, FrameBuilder builder) {
		for (Parameter parameter : parameters)
			builder.add("parameter", new FrameBuilder("parameter").add("name", parameter.name$()).add("type", formatType(parameter.asType())).toFrame());
	}

	private Template template() {
		return Formatters.customize(new JMXAccessorTemplate());
	}

	private Template interfaceTemplate() {
		return Formatters.customize(new JMXServerTemplate());
	}

}
