package teseo.codegeneration.accessor.jms;

import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import teseo.Parameter;
import teseo.Schema;
import teseo.codegeneration.schema.SchemaRenderer;
import teseo.jms.JMSService;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static teseo.helpers.Commons.writeFrame;

public class JMSAccessorRenderer {

	private final JMSService service;
	private File destination;
	private String packageName;


	public JMSAccessorRenderer(JMSService application) {
		this.service = application;
	}

	public void execute(File destination, String packageName) {
		this.destination = destination;
		this.packageName = packageName;
		new SchemaRenderer(service.graph()).execute(destination, packageName);
		processService(service);
	}

	private void processService(JMSService jmsService) {
		Frame frame = new Frame().addTypes("accessor");
		frame.addSlot("name", jmsService.name());
		frame.addSlot("package", packageName);
		if (!jmsService.graph().find(Schema.class).isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		frame.addSlot("resource", (AbstractFrame[]) jmsService.node().findNode(JMSService.Resource.class).stream().
				map(this::processResource).toArray(Frame[]::new));
		writeFrame(destination, snakeCaseToCamelCase(jmsService.name()) + "JMSAccessor", getTemplate().format(frame));
	}

	private Frame processResource(JMSService.Resource resource) {
		final Frame frame = new Frame().addTypes("resource")
				.addSlot("name", resource.name())
				.addSlot("queue", resource.queue())
				.addSlot("parameter", (AbstractFrame[]) parameters(resource.parameterList()))
				.addSlot("messageType", messageType(resource.parameterList()));
		if (resource.response() != null) {
			frame.addTypes("reply");
			frame.addSlot("reply", new Frame().addTypes("reply", resource.response().asType().getClass().getSimpleName()).addSlot("value", resource.response().asType().type()));
		}
		return frame;
	}

	private String messageType(List<Parameter> parameters) {
		for (Parameter parameter : parameters) if (parameter.isFile()) return "Bytes";
		return "Text";
	}

	private Frame[] parameters(List<Parameter> parameters) {
		return parameters.stream().map(this::parameter).toArray(Frame[]::new);
	}

	private Frame parameter(Parameter parameter) {
		return new Frame().addTypes("parameter", parameter.asType().getClass().getSimpleName())
				.addSlot("name", parameter.name())
				.addSlot("type", parameter.asType().type());
	}


	private Template getTemplate() {
		Template template = JMSAccessorTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}
}
