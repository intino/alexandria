package io.intino.konos.builder.codegeneration.accessor.messaging;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.schema.SchemaListRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Parameter;
import io.intino.konos.model.graph.Response;
import io.intino.konos.model.graph.Service;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class MessagingAccessorRenderer extends Renderer {
	private final Service.Messaging service;
	private File destination;

	public MessagingAccessorRenderer(CompilationContext compilationContext, Service.Messaging application, File destination) {
		super(compilationContext, Target.Owner);
		this.service = application;
		this.destination = destination;
	}

	@Override
	public void render() {
		new SchemaListRenderer(context, service.graph(), destination).execute();
		processService(service);
	}

	private void processService(Service.Messaging jmsService) {
		FrameBuilder builder = new FrameBuilder("accessor");
		builder.add("name", jmsService.name$());
		builder.add("package", packageName());
		if (!jmsService.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName()).toFrame());
		final List<Service.Messaging.Request> requests = jmsService.core$().findNode(Service.Messaging.Request.class);
		final Set<String> customParameters = extractCustomParameters(requests);
		builder.add("request", requests.stream().map(request -> processRequest(request, jmsService).toFrame()).toArray(Frame[]::new));
		for (String parameter : customParameters) builder.add("custom", parameter);
		Commons.writeFrame(destination, snakeCaseToCamelCase(jmsService.name$()) + "Accessor", getTemplate().render(builder.toFrame()));
	}

	private Set<String> extractCustomParameters(List<Service.Messaging.Request> requests) {
		Set<String> set = new HashSet<>();
		for (Service.Messaging.Request request : requests) set.addAll(Commons.extractParameters(request.path()));
		return set;
	}

	private FrameBuilder processRequest(Service.Messaging.Request request, Service.Messaging service) {
		final FrameBuilder builder = new FrameBuilder("request")
				.add("name", request.name$())
				.add("service", service.name$())
				.add("path", "service" + chainContext(service.context()) + request.path());
		if (request.parameter() != null)
			builder.add("parameter", parameterFrame(request.parameter()));
		if (request.response() != null && request.response().isType()) {
			builder.add("reply");
			builder.add("response", responseFrame(request.response()));
		}
		return builder;
	}

	private String chainContext(String context) {
		return (context != null && !context.isEmpty() ? "." + context + "." : "");
	}

	private Frame parameterFrame(Parameter parameter) {
		return new FrameBuilder("value")
				.add("name", parameter.name$())
				.add("type", parameter.asType().type()).toFrame();
	}

	private Frame responseFrame(Response response) {
		return new FrameBuilder("value")
				.add("name", response.name$())
				.add("type", response.asType().type()).toFrame();
	}

	private Template getTemplate() {
		return new MessagingAccessorTemplate()
				.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()))
				.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value)
				.add("validname", value -> value.toString().replace("-", "").toLowerCase());
	}
}
