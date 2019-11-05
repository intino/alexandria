package io.intino.konos.builder.codegeneration.action;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.Service.REST.Resource;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RESTResourceActionRenderer extends ActionRenderer {
	private final Resource.Operation operation;

	public RESTResourceActionRenderer(Settings settings, Resource.Operation operation) {
		super(settings, "resource");
		this.operation = operation;
	}

	@Override
	public void render() {
		final String name = firstUpperCase(operation.getClass().getSimpleName()) + firstUpperCase(operation.core$().owner().name());
		classes().put(operation.getClass().getSimpleName() + "#" + firstUpperCase(operation.core$().owner().name()), "actions" + "." + name + "Action");
		execute(name, operation.core$().ownerAs(Service.class).name$(), operation.response(), operation.parameterList(),
				Stream.concat(operation.exceptionList().stream(), operation.exceptionRefs().stream()).collect(Collectors.toList()), operation.graph().schemaList());
	}

	@Override
	protected ContextType contextType() {
		return ContextType.Spark;
	}
}