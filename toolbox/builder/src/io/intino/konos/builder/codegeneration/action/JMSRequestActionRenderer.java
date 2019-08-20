package io.intino.konos.builder.codegeneration.action;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.jms.JMSService.Request;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class JMSRequestActionRenderer extends ActionRenderer {
	private final Request request;

	public JMSRequestActionRenderer(Settings settings, Request request) {
		super(settings, "jms", request.isProcessTrigger() ? "process" : "request");
		this.request = request;
	}

	@Override
	public void render() {
		classes().put(request.getClass().getSimpleName() + "#" + firstUpperCase(request.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(request.name$())) + "Action");
		execute(request.name$(), request.core$().ownerAs(Service.class).name$(), request.response(), request.parameterList(),
				Stream.concat(request.exceptionList().stream(), request.exceptionRefs().stream()).collect(Collectors.toList()), request.graph().schemaList());
	}
}