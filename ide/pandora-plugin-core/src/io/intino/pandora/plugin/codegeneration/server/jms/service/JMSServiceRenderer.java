package io.intino.pandora.plugin.codegeneration.server.jms.service;

import io.intino.pandora.plugin.Parameter;
import io.intino.pandora.plugin.jms.JMSService;
import org.siani.itrules.Template;
import org.siani.itrules.engine.formatters.StringFormatter;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.pandora.plugin.helpers.Commons.writeFrame;

public class JMSServiceRenderer {

	private final List<JMSService> services;
	private File gen;
	private String packageName;

	public JMSServiceRenderer(Graph graph, File gen, String packageName) {
		services = graph.find(JMSService.class);
		this.gen = gen;
		this.packageName = packageName;
	}

	public void execute() {
		services.forEach(this::processService);
	}

	private void processService(JMSService service) {
		Frame frame = new Frame().addTypes("jms").
				addSlot("name", service.name()).
				addSlot("package", packageName).
				addSlot("request", (AbstractFrame[]) processRequests(service.requestList())).
				addSlot("notification", (AbstractFrame[]) processNotifications(service.notificationList()));
		writeFrame(gen, StringFormatter.get().get("firstuppercase").format(service.name()).toString() + "JMSService", template().format(frame));
	}

	private Frame[] processRequests(List<JMSService.Request> requests) {
		return requests.stream().map(this::processRequest).toArray(Frame[]::new);
	}

	private Frame processRequest(JMSService.Request request) {
		return new Frame().addTypes("request").addSlot("name", request.name()).addSlot("queue", request.queue());
	}

	private Frame[] processNotifications(List<JMSService.Notification> notifications) {
		return notifications.stream().map(this::processNotification).toArray(Frame[]::new);
	}

	private Frame processNotification(JMSService.Notification notification) {
		return new Frame().addTypes("notification").
				addSlot("name", notification.name()).
				addSlot("package", packageName).
				addSlot("queue", notification.queue()).
				addSlot("parameter", (AbstractFrame[]) parameters(notification.parameterList())).
				addSlot("returnMessageType", messageType(notification.parameterList()));
	}

	private Template template() {
		Template template = JMSServiceTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
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
