package io.intino.pandora.plugin.codegeneration.server.jms.service;

import io.intino.pandora.plugin.Parameter;
import io.intino.pandora.plugin.Schema;
import io.intino.pandora.plugin.jms.JMSService;
import io.intino.pandora.plugin.jms.JMSService.Notification;
import org.siani.itrules.Template;
import org.siani.itrules.model.AbstractFrame;
import org.siani.itrules.model.Frame;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.pandora.plugin.helpers.Commons.writeFrame;

public class JMSNotifierRenderer {
	private final List<JMSService> services;
	private File gen;
	private String packageName;

	public JMSNotifierRenderer(Graph graph, File gen, String packageName) {
		services = graph.find(JMSService.class);
		this.gen = gen;
		this.packageName = packageName;
	}

	public void execute() {
		services.forEach(this::processService);
	}

	private void processService(JMSService service) {
		processNotifier(service.name(), service.node().findNode(JMSService.Notification.class));
	}

	private void processNotifier(String service, List<Notification> notifications) {
		final Frame notifierFrame = new Frame().addTypes("notifier").addSlot("name", service).addSlot("package", packageName);
		if (!notifications.get(0).graph().find(Schema.class).isEmpty())
			notifierFrame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		for (Notification n : notifications) notifierFrame.addSlot("notification", fillNotificationFrame(n));
		writeFrame(gen, snakeCaseToCamelCase(service) + "Notifier", template().format(notifierFrame));
	}

	private Frame fillNotificationFrame(Notification notification) {
		return new Frame().addTypes("notification").
				addSlot("name", notification.name()).
				addSlot("package", packageName).
				addSlot("queue", notification.queue()).
				addSlot("parameter", (AbstractFrame[]) parameters(notification.parameterList())).
				addSlot("returnMessageType", messageType(notification.parameterList()));
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

	private Template template() {
		Template template = JMSNotifierTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}
}
