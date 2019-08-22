package io.intino.konos.builder.codegeneration.services.messaging;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.action.MessagingRequestActionRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Parameter;
import io.intino.konos.model.graph.Response;
import io.intino.konos.model.graph.messaging.MessagingService;
import io.intino.konos.model.graph.messaging.MessagingService.Request;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MessagingRequestRenderer extends Renderer {
	private static final String REQUESTS = "requests";
	private final List<MessagingService> services;

	public MessagingRequestRenderer(Settings settings, KonosGraph graph) {
		super(settings, Target.Owner);
		this.services = graph.messagingServiceList();
	}

	@Override
	public void render() {
		services.forEach(this::processService);
	}

	private void processService(MessagingService service) {
		service.core$().findNode(Request.class).forEach(this::processRequest);
	}

	private void processRequest(Request request) {
		if (!request.isProcessTrigger())
			writeFrame(new File(gen(), REQUESTS), snakeCaseToCamelCase(request.name$()) + "Request", template().render(fillRequestFrame(request)));
		createCorrespondingAction(request);
	}

	private void createCorrespondingAction(Request request) {
		new MessagingRequestActionRenderer(settings, request).execute();
	}

	private Frame fillRequestFrame(Request request) {
		final String returnType = Commons.returnType(request.response());
		Frame[] parameters = parameters(request.parameterList());
		FrameBuilder builder = new FrameBuilder("request").
				add("name", request.name$()).
				add("box", boxName()).
				add("package", packageName()).
				add("call", call(returnType, parameters));
		if (parameters.length > 0) builder.add("parameter", parameters);
		if (!returnType.equals("void"))
			builder.add("returnType", returnType).add("returnMessageType", messageType(request.response()));
		if (!request.exceptionList().isEmpty() || !request.exceptionRefs().isEmpty())
			builder.add("exception", "");
		if (!request.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName()).toFrame());
		return builder.toFrame();
	}

	@NotNull
	private Frame call(String returnType, Frame[] parameters) {
		FrameBuilder builder = new FrameBuilder(returnType, "call");
		if (parameters.length > 0) builder.add("parameter", parameters);
		return builder.toFrame();
	}

	private String messageType(Response response) {
		return response.isFile() ? "Bytes" : "Text";
	}

	private Frame[] parameters(List<Parameter> parameters) {
		return parameters.stream().map(this::parameter).toArray(Frame[]::new);
	}

	private Frame parameter(Parameter parameter) {
		final FrameBuilder builder = new FrameBuilder("parameter", parameter.asType().getClass().getSimpleName());
		if (parameter.isList()) builder.add("List");
		return builder.add("name", parameter.name$()).add("type", parameter.asType().type()).toFrame();
	}

	private Template template() {
		return Formatters.customize(new MessagingRequestTemplate());
	}
}
