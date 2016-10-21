package io.intino.pandora.plugin.codegeneration.action;

import io.intino.pandora.plugin.Schema;
import io.intino.pandora.plugin.jms.JMSService;

import java.io.File;
import java.util.Collections;

public class JMSNotificationActionRenderer extends ActionRenderer {
	private final JMSService.Notification notification;

	public JMSNotificationActionRenderer(JMSService.Notification notification, File destiny, String packageName) {
		super(destiny, packageName);
		this.notification = notification;
	}

	public void execute() {
		execute(notification.name(), null, notification.parameterList(), Collections.emptyList(), notification.graph().find(Schema.class));
	}
}