package io.intino.pandora.model.codegeneration.accessor.jmx;

import io.intino.pandora.model.Parameter;
import io.intino.pandora.model.Schema;
import io.intino.pandora.model.codegeneration.schema.SchemaRenderer;
import io.intino.pandora.model.codegeneration.server.jmx.JMXServerTemplate;
import io.intino.pandora.model.helpers.Commons;
import io.intino.pandora.model.jmx.JMXService;
import io.intino.pandora.model.object.ObjectData;
import io.intino.pandora.model.type.TypeData;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class JMXAccessorRenderer {
	private final JMXService service;
	private File destination;
	private String packageName;

	public JMXAccessorRenderer(JMXService restService, File destination, String packageName) {
		this.service = restService;
		this.destination = destination;
		this.packageName = packageName;
	}

	public void execute() {
		new SchemaRenderer(service.graph(), destination, packageName).execute();
		createInterface(service);
		createService(service);
	}

	private void createInterface(JMXService service) {
		Frame frame = new Frame().addTypes("jmx", "interface");
		fillFrame(service, frame);
		Commons.writeFrame(destinationPackage(), service.name() + "MBean", interfaceTemplate().format(frame));
	}

	private File destinationPackage() {
		return new File(destination, "jmx");
	}

	private void createService(JMXService service) {
		Frame frame = new Frame().addTypes("accessor");
		fillFrame(service, frame);
		Commons.writeFrame(destination, snakeCaseToCamelCase(service.name()) + "JMXAccessor", template().format(frame));
	}

	private void fillFrame(JMXService service, Frame frame) {
		frame.addSlot("name", service.name());
		frame.addSlot("package", packageName);
		if (!service.graph().find(Schema.class).isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		for (JMXService.Operation operation : service.operationList())
			frame.addSlot("operation", frameOf(operation));
	}

	private Frame frameOf(JMXService.Operation operation) {
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
		Template template = JMXAccessorTemplate.create();
		format(template);
		return template;
	}

	private Template interfaceTemplate() {
		Template template = JMXServerTemplate.create();
		format(template);
		return template;
	}

	private void format(Template template) {
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> {
			if (value.equals("Void")) return "void";
			else if (value.toString().contains(".")) return firstLowerCase(value.toString());
			else return value;
		});
		template.add("ValidPackage", Commons::validPackage);
	}

	public static String firstLowerCase(String value) {
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}

}
