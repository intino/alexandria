package io.intino.konos.builder.codegeneration.server.jms.service;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.model.Parameter;
import io.intino.konos.model.jms.JMSService;
import io.intino.konos.builder.helpers.Commons;
import io.intino.tara.magritte.Graph;
import org.siani.itrules.Template;
import org.siani.itrules.engine.formatters.StringFormatter;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

public class JMSServiceRenderer {

	private final List<JMSService> services;
	private File gen;
	private String packageName;
	private final String boxName;

	public JMSServiceRenderer(Graph graph, File gen, String packageName, String boxName) {
		services = graph.find(JMSService.class);
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		services.forEach(this::processService);
	}

	private void processService(JMSService service) {
		Frame frame = new Frame().addTypes("jms").
				addSlot("name", service.name()).
				addSlot("box", boxName).
				addSlot("package", packageName).
				addSlot("model", service.subscriptionModel().name()).
				addSlot("request", (AbstractFrame[]) processRequests(service.requestList(), service.subscriptionModel().name())).
				addSlot("notification", (AbstractFrame[]) processNotifications(service.notificationList(), service.subscriptionModel().name()));
		Commons.writeFrame(gen, StringFormatter.get().get("firstuppercase").format(service.name()).toString() + "Service", template().format(frame));
	}

	private Frame[] processRequests(List<JMSService.Request> requests, String subscriptionModel) {
		return requests.stream().map((request) -> processRequest(request, subscriptionModel)).toArray(Frame[]::new);
	}

	private Frame processRequest(JMSService.Request request, String subscriptionModel) {
		return new Frame().addTypes("request").
				addSlot("name", request.name()).
				addSlot("model", subscriptionModel).
				addSlot("queue", customize(request.path()));
	}

	private Frame customize(String queue) {
		Frame frame = new Frame().addTypes("queue");
		frame.addSlot("name", queue);
		for (String parameter : Commons.extractParameters(queue)) frame.addSlot("custom", parameter);
		return frame;
	}

	private Frame[] processNotifications(List<JMSService.Notification> notifications, String subscriptionModel) {
		return notifications.stream().map((notification) -> processNotification(notification, subscriptionModel)).toArray(Frame[]::new);
	}

	private Frame processNotification(JMSService.Notification notification, String subscriptionModel) {
		return new Frame().addTypes("notification").
				addSlot("name", notification.name()).
				addSlot("package", packageName).
				addSlot("queue", customize(notification.path())).
				addSlot("model", subscriptionModel).
				addSlot("parameter", (AbstractFrame[]) parameters(notification.parameterList())).
				addSlot("returnMessageType", messageType(notification.parameterList()));
	}

	private Template template() {
		return Formatters.customize(JMSServiceTemplate.create());
	}

	private String messageType(List<Parameter> parameters) {
		return parameters.stream().filter(Parameter::isFile).count() > 0 ? "Bytes" : "Text";
	}

	private Frame[] parameters(List<Parameter> parameters) {
		return parameters.stream().map(this::parameter).toArray(Frame[]::new);
	}

	private Frame parameter(Parameter parameter) {
		return new Frame().addTypes("parameter", parameter.asType().getClass().getSimpleName())
				.addSlot("name", parameter.name())
				.addSlot("type", parameter.asType().type());
	}

}
