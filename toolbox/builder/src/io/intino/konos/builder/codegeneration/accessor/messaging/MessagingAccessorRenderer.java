package io.intino.konos.builder.codegeneration.accessor.messaging;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.schema.SchemaListRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Parameter;
import io.intino.konos.model.graph.Workflow;
import io.intino.konos.model.graph.messaging.MessagingService;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class MessagingAccessorRenderer extends Renderer {
	private final MessagingService service;
	private final Workflow workflow;
	private File destination;

	public MessagingAccessorRenderer(Settings settings, MessagingService application, Workflow workflow, File destination) {
		super(settings, Target.Owner);
		this.service = application;
		this.workflow = workflow;
		this.destination = destination;
	}

	@Override
	public void render() {
		new SchemaListRenderer(settings, service.graph(), destination).execute();
		processService(service);
	}

	private void processService(MessagingService jmsService) {
		FrameBuilder builder = new FrameBuilder("accessor");
		builder.add("name", jmsService.name$());
		builder.add("package", packageName());
		if (workflow != null) builder.add("businessUnit", workflow.businessUnit());
		if (!jmsService.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName()).toFrame());
		final List<MessagingService.Request> requests = jmsService.core$().findNode(MessagingService.Request.class);
		if (requests.stream().anyMatch(MessagingService.Request::isProcessTrigger)) builder.add("hasProcess", ";");
		final Set<String> customParameters = extractCustomParameters(requests);
		builder.add("request", requests.stream().map(request -> processRequest(request, customParameters).toFrame()).toArray(Frame[]::new));
		for (String parameter : customParameters) builder.add("custom", parameter);
		Commons.writeFrame(destination, snakeCaseToCamelCase(jmsService.name$()) + "Accessor", getTemplate().render(builder.toFrame()));
	}

	private Set<String> extractCustomParameters(List<MessagingService.Request> requests) {
		Set<String> set = new HashSet<>();
		for (MessagingService.Request request : requests) set.addAll(Commons.extractParameters(request.path()));
		return set;
	}

	private FrameBuilder processRequest(MessagingService.Request request, Set<String> customParameters) {
		final FrameBuilder builder = new FrameBuilder("request")
				.add("name", request.name$())
				.add("queue", request.path())
				.add("parameter", parameters(request.parameterList()))
				.add("messageType", messageType(request.parameterList()));
		if (request.response() != null && request.response().isType()) {
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
		return new MessagingAccessorTemplate()
				.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()))
				.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value)
				.add("validname", value -> value.toString().replace("-", "").toLowerCase());
	}
}
