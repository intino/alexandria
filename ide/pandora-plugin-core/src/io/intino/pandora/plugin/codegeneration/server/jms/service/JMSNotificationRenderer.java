package io.intino.pandora.plugin.codegeneration.server.jms.service;

import io.intino.pandora.plugin.Parameter;
import io.intino.pandora.plugin.Schema;
import io.intino.pandora.plugin.codegeneration.action.JMSNotificationActionRenderer;
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

public class JMSNotificationRenderer {
	private static final String NOTIFICATIONS = "notifications";
	private final List<JMSService> services;
	private File gen;
	private File src;
	private String packageName;

	public JMSNotificationRenderer(Graph graph, File src, File gen, String packageName) {
		services = graph.find(JMSService.class);
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
	}

	public void execute() {
		services.forEach(this::processService);
	}

	private void processService(JMSService service) {
		service.node().findNode(JMSService.Notification.class).forEach(this::processNotification);
	}

	private void processNotification(Notification resource) {
		Frame frame = fillNotificationFrame(resource);
		writeFrame(new File(gen, NOTIFICATIONS), snakeCaseToCamelCase(resource.name()) + "Notification", template().format(frame));
		createCorrespondingAction(resource);
	}

	private void createCorrespondingAction(Notification notification) {
		new JMSNotificationActionRenderer(notification, src, packageName).execute();
	}

	private Frame fillNotificationFrame(Notification notification) {
		Frame frame = new Frame().addTypes("notification").
				addSlot("name", notification.name()).
				addSlot("package", packageName).
				addSlot("queue", notification.queue()).
				addSlot("parameter", (AbstractFrame[]) parameters(notification.parameterList())).
				addSlot("returnMessageType", messageType(notification.parameterList()));

		if (!notification.graph().find(Schema.class).isEmpty())
			frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
		return frame;
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
		Template template = JMSNotificationTemplate.create();
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}
}
