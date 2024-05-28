package io.intino.konos.builder.codegeneration.accessor.messaging;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.schema.SchemaListRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.dsl.Parameter;
import io.intino.konos.dsl.Response;
import io.intino.konos.dsl.Service;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class MessagingAccessorRenderer extends Renderer {
	private final Service.Messaging service;
	private final File destination;

	public MessagingAccessorRenderer(CompilationContext compilationContext, Service.Messaging application, File destination) {
		super(compilationContext);
		this.service = application;
		this.destination = destination;
	}

	@Override
	public void render() throws KonosException {
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
		Commons.writeFrame(destination, snakeCaseToCamelCase(jmsService.name$()) + "Accessor", new MessagingAccessorTemplate().render(builder.toFrame(), Formatters.all));
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
}