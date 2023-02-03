package io.intino.konos.builder.codegeneration.services.messaging;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.action.MessagingRequestActionRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.KonosGraph;
import io.intino.konos.model.Parameter;
import io.intino.konos.model.Response;
import io.intino.konos.model.Service;
import io.intino.konos.model.Service.Messaging.Request;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MessagingRequestRenderer extends Renderer {
	private static final String REQUESTS = "requests";
	private final List<Service.Messaging> services;

	public MessagingRequestRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext);
		this.services = graph.serviceList(Service::isMessaging).map(Service::asMessaging).collect(Collectors.toList());
	}

	@Override
	public void render() throws KonosException {
		for (Service.Messaging service : services) processService(service);
	}

	private void processService(Service.Messaging service) throws KonosException {
		for (Request request : service.core$().findNode(Request.class)) processRequest(request);
	}

	private void processRequest(Request request) throws KonosException {
		File packageFolder = new File(gen(Target.Server), REQUESTS);
		writeFrame(packageFolder, snakeCaseToCamelCase(request.name$()) + "Request", template().render(fillRequestFrame(request)));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(request), javaFile(packageFolder, snakeCaseToCamelCase(request.name$()) + "Request").getAbsolutePath()));
		createCorrespondingAction(request);
	}

	private void createCorrespondingAction(Request request) throws KonosException {
		new MessagingRequestActionRenderer(context, request).execute();
	}

	private Frame fillRequestFrame(Request request) {
		final String returnType = Commons.returnType(request.response());
		Frame parameter = parameterFrame(request.parameter());
		FrameBuilder builder = new FrameBuilder("request").
				add("name", request.name$()).
				add("box", boxName()).
				add("package", packageName()).
				add("call", call(returnType, parameter));
		if (parameter != null) builder.add("parameter", parameter);
		if (!returnType.equals("void"))
			builder.add("returnType", returnType).add("returnMessageType", messageType(request.response()));
		if (!request.exceptionList().isEmpty()) builder.add("exception", "");
		if (!request.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName()).toFrame());
		return builder.toFrame();
	}


	private Frame call(String returnType, Frame parameter) {
		FrameBuilder builder = new FrameBuilder(returnType, "call");
		if (parameter != null) builder.add("parameter", parameter);
		return builder.toFrame();
	}

	private String messageType(Response response) {
		return response.isFile() ? "Bytes" : "Text";
	}

	private Frame parameterFrame(Parameter parameter) {
		if (parameter == null) return null;
		return new FrameBuilder("parameter", parameter.asType().getClass().getSimpleName())
				.add("name", parameter.name$())
				.add("type", parameter.asType().type()).toFrame();
	}

	private Template template() {
		return Formatters.customize(new MessagingRequestTemplate());
	}
}
