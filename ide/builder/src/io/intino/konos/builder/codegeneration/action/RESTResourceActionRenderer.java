package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.rest.RESTService.Resource;

import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RESTResourceActionRenderer extends ActionRenderer {
	private final Resource.Operation operation;
	private final Map<String, String> classes;

	public RESTResourceActionRenderer(Project project, Resource.Operation operation, File destiny, String packageName, String boxName, Map<String, String> classes) {
		super(project, destiny, packageName, boxName, "resource");
		this.operation = operation;
		this.classes = classes;
	}

	public void execute() {
		final String name = firstUpperCase(operation.getClass().getSimpleName()) + firstUpperCase(operation.core$().owner().name());
		classes.put(operation.getClass().getSimpleName() + "#" + firstUpperCase(operation.core$().owner().name()), "actions" + "." + name + "Action");
		execute(name, operation.response(), operation.parameterList(),
				Stream.concat(operation.exceptionList().stream(), operation.exceptionRefs().stream()).collect(Collectors.toList()), operation.graph().schemaList());
	}
}