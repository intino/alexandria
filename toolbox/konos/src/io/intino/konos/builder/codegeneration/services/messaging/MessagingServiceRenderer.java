package io.intino.konos.builder.codegeneration.services.messaging;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.formatters.StringFormatters;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.KonosGraph;
import io.intino.konos.dsl.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.all;
import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MessagingServiceRenderer extends Renderer {
	private final List<Service.Messaging> services;

	public MessagingServiceRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext);
		this.services = graph.serviceList(Service::isMessaging).map(Service::asMessaging).collect(Collectors.toList());
	}

	@Override
	public void render() {
		services.forEach(this::processService);
	}

	private void processService(Service.Messaging service) {
		FrameBuilder builder = frameBuilder(service);
		writeFrame(gen(Target.Service), nameOf(service), new MessagingServiceTemplate().render(builder.toFrame(), all));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(service), javaFile(gen(Target.Service), nameOf(service)).getAbsolutePath()));
	}

	private FrameBuilder frameBuilder(Service.Messaging service) {
		FrameBuilder builder = new FrameBuilder("messaging").
				add("name", service.name$()).
				add("box", boxName()).
				add("package", packageName()).
				add("model", service.subscriptionModel().name()).
				add("request", processRequests(service.requestList(), service.context(), service.subscriptionModel().name()));
		if (!service.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName()).toFrame());
		return builder;
	}

	private String nameOf(Service.Messaging service) {
		return StringFormatters.get(Locale.getDefault()).get("firstuppercase").format(service.name$()).toString() + "Service";
	}

	private Frame[] processRequests(List<Service.Messaging.Request> requests, String domain, String subscriptionModel) {
		return requests.stream().map((request) -> processRequest(request, domain, subscriptionModel)).toArray(Frame[]::new);
	}

	private Frame processRequest(Service.Messaging.Request request, String context, String subscriptionModel) {
		FrameBuilder builder = new FrameBuilder("request").
				add("name", request.name$()).
				add("package", packageName()).
				add("model", subscriptionModel).
				add("path", customize("path", "service." + context + "." + request.path()));
		return builder.toFrame();
	}
}