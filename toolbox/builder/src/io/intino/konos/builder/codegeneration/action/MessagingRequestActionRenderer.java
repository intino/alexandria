package io.intino.konos.builder.codegeneration.action;

import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.Service.Messaging.Request;

import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class MessagingRequestActionRenderer extends ActionRenderer {
	private final Request request;

	public MessagingRequestActionRenderer(CompilationContext compilationContext, Request request) {
		super(compilationContext, "jms", "request");
		this.request = request;
	}

	@Override
	public void render() {
		classes().put(request.getClass().getSimpleName() + "#" + firstUpperCase(request.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(request.name$())) + "Action");
		execute(request.name$(), request.core$().ownerAs(Service.class).name$(), request.response(), List.of(request.parameter()),
				request.exceptionList(), request.graph().schemaList());
	}
}