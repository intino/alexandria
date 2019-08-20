package io.intino.konos.builder.codegeneration.services.jms;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.itrules.formatters.StringFormatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.model.graph.BusinessUnit;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Parameter;
import io.intino.konos.model.graph.jms.JMSService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class JMSServiceRenderer extends Renderer {
	private final List<JMSService> services;
	private final BusinessUnit businessUnit;

	public JMSServiceRenderer(Settings settings, KonosGraph graph) {
		super(settings, Target.Owner);
		this.services = graph.jMSServiceList();
		this.businessUnit = graph.businessUnit();
	}

	@Override
	public void render() {
		services.forEach(this::processService);
	}

	private void processService(JMSService service) {
		FrameBuilder builder = new FrameBuilder("jms").
				add("name", service.name$()).
				add("box", boxName()).
				add("package", packageName()).
				add("businessUnit", businessUnit != null ? businessUnit.name() : "").
				add("model", service.subscriptionModel().name()).
				add("request", processRequests(service.requestList(), service.subscriptionModel().name())).
				add("notification", processNotifications(service.notificationList(), service.subscriptionModel().name()));
		if (service.requestList().stream().anyMatch(JMSService.Request::isProcessTrigger))
			builder.add("hasProcess", ";");
		if (!service.graph().schemaList().isEmpty())
			builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName()).toFrame());
		writeFrame(gen(), nameOf(service), template().render(builder.toFrame()));
	}

	@NotNull
	private String nameOf(JMSService service) {
		return StringFormatters.get(Locale.getDefault()).get("firstuppercase").format(service.name$()).toString() + "Service";
	}

	private Frame[] processRequests(List<JMSService.Request> requests, String subscriptionModel) {
		return requests.stream().map((request) -> processRequest(request, subscriptionModel)).toArray(Frame[]::new);
	}

	private Frame processRequest(JMSService.Request request, String subscriptionModel) {
		FrameBuilder builder = new FrameBuilder("request").
				add("name", request.name$()).
				add("model", subscriptionModel).
				add("parameter", parameters(request.parameterList())).
				add("queue", customize("queue", request.path()));
		if (request.isProcessTrigger())
			builder.add("process").add("process", request.asProcessTrigger().process().name$()).add("package", packageName());
		return builder.toFrame();
	}

	private Frame[] processNotifications(List<JMSService.Notification> notifications, String subscriptionModel) {
		return notifications.stream().map((notification) -> processNotification(notification, subscriptionModel)).toArray(Frame[]::new);
	}

	private Frame processNotification(JMSService.Notification notification, String subscriptionModel) {
		return new FrameBuilder("notification").
				add("name", notification.name$()).
				add("package", packageName()).
				add("queue", customize("queue", notification.path())).
				add("model", subscriptionModel).
				add("parameter", parameters(notification.parameterList())).
				add("returnMessageType", messageType(notification.parameterList())).toFrame();
	}

	private Template template() {
		return customize(new JmsServiceTemplate());
	}

	private String messageType(List<Parameter> parameters) {
		return parameters.stream().anyMatch(Parameter::isFile) ? "Bytes" : "Text";
	}

	private Frame[] parameters(List<Parameter> parameters) {
		return parameters.stream().map(this::parameter).toArray(Frame[]::new);
	}

	private Frame parameter(Parameter parameter) {
		return new FrameBuilder("parameter", parameter.asType().getClass().getSimpleName())
				.add("name", parameter.name$())
				.add("type", parameter.asType().type()).toFrame();
	}

}
