package io.intino.konos.builder.codegeneration.action;

import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.Service.JMX.Operation;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class JMXActionRenderer extends ActionRenderer {
	private final Operation operation;

	public JMXActionRenderer(CompilationContext compilationContext, Operation operation) {
		super(compilationContext, "jmx");
		this.operation = operation;
	}

	@Override
	public void render() {
		classes().put(operation.getClass().getSimpleName() + "#" + firstUpperCase(operation.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(operation.name$())) + "Action");
		execute(operation.name$(), operation.core$().ownerAs(Service.class).name$(), operation.response(), operation.parameterList(), operation.exceptionList(), operation.graph().schemaList());
	}
}
