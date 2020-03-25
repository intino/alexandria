package io.intino.konos.builder.codegeneration.action;

import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.model.graph.Service;

import java.util.Collections;
import java.util.Map;

public class SoapOperationActionRenderer extends ActionRenderer {

	private final Service.Soap.Operation operation;

	public SoapOperationActionRenderer(CompilationContext context, Service.Soap.Operation operation) {
		super(context, "Operation");
		this.operation = operation;
	}

	@Override
	protected void render() {
		final String name = firstUpperCase(operation.name$());
		classes().put(operation.getClass().getSimpleName() + "#" + firstUpperCase(operation.core$().owner().name()), "actions" + "." + name + "Action");
		execute(name, operation.core$().ownerAs(Service.class).name$(), operation.output(), Map.of(operation.input().asObject().type(), operation.input()), Collections.emptyList(), operation.graph().schemaList());
	}
}
