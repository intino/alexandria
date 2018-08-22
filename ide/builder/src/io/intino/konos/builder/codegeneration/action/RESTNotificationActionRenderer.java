package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.Exception;
import io.intino.konos.model.graph.rest.RESTService.Notification;

import java.io.File;
import java.util.Collections;
import java.util.Map;

public class RESTNotificationActionRenderer extends ActionRenderer {
	private final Notification notification;
	private final Map<String, String> classes;

	public RESTNotificationActionRenderer(Project project, Notification notification, File destiny, String packageName, String boxName, Map<String, String> classes) {
		super(project, destiny, packageName, boxName);
		this.notification = notification;
		this.classes = classes;
	}

	public void execute() {
		final String name = firstUpperCase(notification.name$());
		classes.put(notification.getClass().getSimpleName() + "#" + firstUpperCase(notification.core$().owner().name()), "actions" + "." + name + "Action");
		execute(name, null, notification.parameterList(), Collections.emptyList(), notification.graph().schemaList());
	}

}