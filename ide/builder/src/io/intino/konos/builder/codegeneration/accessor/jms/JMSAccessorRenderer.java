package io.intino.konos.builder.codegeneration.accessor.jms;

import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Parameter;
import io.intino.konos.model.Schema;
import io.intino.konos.model.jms.JMSService;
import io.intino.konos.builder.codegeneration.schema.SchemaRenderer;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

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
		final List<JMSService.Request> requests = jmsService.node().findNode(JMSService.Request.class);
		final Set<String> customParameters = extractCustomParameters(requests);
		frame.addSlot("request", (AbstractFrame[]) requests.stream().
				map(request -> processRequest(request, customParameters)).toArray(Frame[]::new));
		for (String parameter : customParameters) frame.addSlot("custom", parameter);
		Commons.writeFrame(destination, snakeCaseToCamelCase(jmsService.name()) + "Accessor", getTemplate().format(frame));
	}

	private Set<String> extractCustomParameters(List<JMSService.Request> requests) {
		Set<String> set = new HashSet<>();
		for (JMSService.Request request : requests) set.addAll(Commons.extractParameters(request.path()));
		return set;
	}

	private Frame processRequest(JMSService.Request request, Set<String> customParameters) {
		final Frame frame = new Frame().addTypes("request")
				.addSlot("name", request.name())
				.addSlot("queue", request.path())
				.addSlot("parameter", (AbstractFrame[]) parameters(request.parameterList()))
				.addSlot("messageType", messageType(request.parameterList()));
		if (request.response() != null) {
			frame.addTypes("reply");
			final Frame reply = new Frame();
			if (request.response().isList()) reply.addTypes("list");
			frame.addSlot("reply", reply.addTypes("reply", request.response().asType().getClass().getSimpleName()).addSlot("value", request.response().asType().type()));
		}
		for (String parameter : customParameters) frame.addSlot("custom", parameter);

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
