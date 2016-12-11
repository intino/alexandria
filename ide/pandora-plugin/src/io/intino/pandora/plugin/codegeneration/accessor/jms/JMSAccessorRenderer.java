package io.intino.pandora.plugin.codegeneration.accessor.jms;

import io.intino.pandora.model.Parameter;
import io.intino.pandora.model.Schema;
import io.intino.pandora.model.jms.JMSService;
import io.intino.pandora.plugin.codegeneration.schema.SchemaRenderer;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.pandora.plugin.helpers.Commons.writeFrame;

public class JMSAccessorRenderer {

	private final JMSService service;
	private File destination;
	private String packageName;


	public JMSAccessorRenderer(JMSService application, File destination, String packageName) {
		this.service = application;
		this.destination = destination;
		this.packageName = packageName;
	}

	public void execute() {
		new SchemaRenderer(service.graph(), destination, packageName).execute();
		processService(service);
	}

	private void processService(JMSService jmsService) {
		Frame frame = new Frame().addTypes("accessor");
		frame.addSlot("name", jmsService.name());
		frame.addSlot("package", packageName);
		if (!jmsService.graph().find(Schema.class).isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		frame.addSlot("request", (AbstractFrame[]) jmsService.node().findNode(JMSService.Request.class).stream().
				map(this::processRequest).toArray(Frame[]::new));
		writeFrame(destination, snakeCaseToCamelCase(jmsService.name()) + "JMSAccessor", getTemplate().format(frame));
	}

	private Frame processRequest(JMSService.Request request) {
		final Frame frame = new Frame().addTypes("request")
				.addSlot("name", request.name())
				.addSlot("queue", request.path())
				.addSlot("parameter", (AbstractFrame[]) parameters(request.parameterList()))
				.addSlot("messageType", messageType(request.parameterList()));
		if (request.response() != null) {
			frame.addTypes("reply");
			frame.addSlot("reply", new Frame().addTypes("reply", request.response().asType().getClass().getSimpleName()).addSlot("value", request.response().asType().type()));
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
