package io.intino.konos.builder.codegeneration.action;

import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.Service;
import io.intino.konos.dsl.Service.JMX.Operation;

import static io.intino.itrules.formatters.StringFormatters.camelCase;
import static io.intino.itrules.formatters.StringFormatters.firstUpperCase;

public class JMXActionRenderer extends ActionRenderer {
	private final Operation operation;

	public JMXActionRenderer(CompilationContext compilationContext, Operation operation) {
		super(compilationContext, "jmx");
		this.operation = operation;
	}

	@Override
	public void render() {
		classes().put(operation.getClass().getSimpleName() + "#" + firstUpperCase().format(operation.core$().name()), "actions" + "." + firstUpperCase().format((camelCase().format((operation.name$())) + "Action")));
		execute(operation.name$(), operation.core$().ownerAs(Service.class).name$(), operation.response(), operation.parameterList(), operation.exceptionList(), operation.graph().schemaList());
	}
}
