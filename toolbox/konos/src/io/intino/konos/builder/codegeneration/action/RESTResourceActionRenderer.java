package io.intino.konos.builder.codegeneration.action;

import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.Service;
import io.intino.konos.dsl.Service.REST.Resource;
import io.intino.magritte.framework.Node;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;

public class RESTResourceActionRenderer extends ActionRenderer {
	private final Resource.Operation operation;

	public RESTResourceActionRenderer(CompilationContext compilationContext, Resource.Operation operation) {
		super(compilationContext, "resource", "rest");
		this.operation = operation;
	}

	@Override
	public void render() {
		Node resource = operation.core$().owner();
		final String name = firstUpperCase(operation.getClass().getSimpleName()) + firstUpperCase(resource.name());
		classes().put(operation.getClass().getSimpleName() + "#" + firstUpperCase(resource.name()), "actions" + "." + name + "Action");
		execute(name, operation.core$().ownerAs(Service.class).name$(), operation.response(), parameters(resource),
				Stream.concat(operation.exceptionList().stream(), operation.exceptionRefs().stream()).collect(Collectors.toList()), operation.graph().schemaList());
	}

	private List<Resource.Parameter> parameters(Node resource) {
		return Stream.concat(resource.as(Resource.class).parameterList().stream(), operation.parameterList().stream()).collect(Collectors.toList());
	}

	@Override
	protected ContextType contextType() {
		return ContextType.Server;
	}
}