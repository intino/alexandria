package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.rest.RESTService.Resource;

import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RESTActionRenderer extends ActionRenderer {
	private final Resource.Operation operation;

	public RESTActionRenderer(Project project, Resource.Operation operation, File destiny, String packageName, String boxName) {
		super(project, destiny, packageName, boxName);
		this.operation = operation;
	}

	public void execute() {
		execute(firstUpperCase(operation.getClass().getSimpleName()) + firstUpperCase(operation.core$().owner().name()),
				operation.response(), operation.parameterList(),
				Stream.concat(operation.exceptionList().stream(), operation.exceptionRefs().stream()).collect(Collectors.toList()), operation.graph().schemaList());
	}
}
