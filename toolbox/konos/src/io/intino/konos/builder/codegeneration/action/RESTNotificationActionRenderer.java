package io.intino.konos.builder.codegeneration.action;

import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.Service;
import io.intino.konos.dsl.Service.REST.Notification;

import java.util.Collections;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;

public class RESTNotificationActionRenderer extends ActionRenderer {
	private final Notification notification;

	public RESTNotificationActionRenderer(CompilationContext compilationContext, Notification notification) {
		super(compilationContext, "notification");
		this.notification = notification;
	}

	@Override
	public void render() {
		final String name = firstUpperCase(notification.name$());
		classes().put(notification.getClass().getSimpleName() + "#" + firstUpperCase(notification.core$().owner().name()), "actions" + "." + name + "Action");
		execute(name, notification.core$().ownerAs(Service.class).name$(), null, notification.parameterList(), Collections.emptyList(), notification.graph().schemaList());
	}

	@Override
	protected ContextType contextType() {
		return ContextType.Spark;
	}
}