package io.intino.konos.builder.codegeneration.action;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.rest.RESTService.Notification;

import java.util.Collections;

public class RESTNotificationActionRenderer extends ActionRenderer {
	private final Notification notification;

	public RESTNotificationActionRenderer(Settings settings, Notification notification) {
		super(settings, "notification");
		this.notification = notification;
	}

	@Override
	public void render() {
		final String name = firstUpperCase(notification.name$());
		classes().put(notification.getClass().getSimpleName() + "#" + firstUpperCase(notification.core$().owner().name()), "actions" + "." + name + "Action");
		execute(name, notification.core$().ownerAs(Service.class).name$(), null, notification.parameterList(), Collections.emptyList(), notification.graph().schemaList());
	}

}