package io.intino.konos.builder.codegeneration.accessor.jms;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.schema.SchemaListRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.BusinessUnit;
import io.intino.konos.model.graph.Parameter;
import io.intino.konos.model.graph.jms.JMSService;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class JMSAccessorRenderer extends Renderer {
	private final JMSService service;
	private final BusinessUnit businessUnit;
	private File destination;

	public JMSAccessorRenderer(Settings settings, JMSService application, BusinessUnit businessUnit, File destination) {
		super(settings, Target.Owner);
		this.service = application;
		this.businessUnit = businessUnit;
		this.destination = destination;
	}

	@Override
	public void render() {
		new SchemaListRenderer(settings, service.graph(), destination).execute();
		processService(service);
	}

	private void processService(JMSService jmsService) {
		FrameBuilder builder = new FrameBuilder("accessor");
		builder.add("name", jmsService.name$());
		builder.add("package", packageName());
		builder.add("businessUnit", businessUnit.name());
		if (!jmsService.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName()).toFrame());
		final List<JMSService.Request> requests = jmsService.core$().findNode(JMSService.Request.class);
		if (requests.stream().anyMatch(JMSService.Request::isProcessTrigger)) builder.add("hasProcess", ";");
		final Set<String> customParameters = extractCustomParameters(requests);
		builder.add("request", requests.stream().map(request -> processRequest(request, customParameters).toFrame()).toArray(Frame[]::new));
		for (String parameter : customParameters) builder.add("custom", parameter);
		Commons.writeFrame(destination, snakeCaseToCamelCase(jmsService.name$()) + "Accessor", getTemplate().render(builder.toFrame()));
	}

	private Set<String> extractCustomParameters(List<JMSService.Request> requests) {
		Set<String> set = new HashSet<>();
		for (JMSService.Request request : requests) set.addAll(Commons.extractParameters(request.path()));
		return set;
	}

	private FrameBuilder processRequest(JMSService.Request request, Set<String> customParameters) {
		final FrameBuilder builder = new FrameBuilder("request")
				.add("name", request.name$())
				.add("queue", request.path())
				.add("parameter", parameters(request.parameterList()))
				.add("messageType", messageType(request.parameterList()));
		if (request.response() != null) {
			builder.add("reply");
			final FrameBuilder reply = new FrameBuilder();
			if (request.response().isList()) reply.add("list");
			builder.add("reply", reply.add("reply").add(request.response().asType().getClass().getSimpleName()).add("value", request.response().asType().type()));
		}
		customParameters.forEach(parameter -> builder.add("custom", parameter));
		return builder;
	}

	private String messageType(List<Parameter> parameters) {
		for (Parameter parameter : parameters) if (parameter.isFile()) return "Bytes";
		return "Text";
	}

	private Frame[] parameters(List<Parameter> parameters) {
		return parameters.stream().map(this::parameter).toArray(Frame[]::new);
	}

	private Frame parameter(Parameter parameter) {
		return new FrameBuilder("parameter", parameter.asType().getClass().getSimpleName())
				.add("name", parameter.name$())
				.add("type", parameter.asType().type()).toFrame();
	}


	private Template getTemplate() {
		return new JMSAccessorTemplate()
				.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()))
				.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value)
				.add("validname", value -> value.toString().replace("-", "").toLowerCase());
	}
}
