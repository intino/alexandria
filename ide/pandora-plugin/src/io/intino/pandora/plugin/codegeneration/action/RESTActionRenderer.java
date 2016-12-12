package io.intino.pandora.plugin.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.pandora.model.Schema;
import io.intino.pandora.model.rest.RESTService.Resource;

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
		execute(firstUpperCase(operation.concept().name()) + firstUpperCase(operation.owner().name()),
				operation.response(), operation.parameterList(),
				Stream.concat(operation.exceptionList().stream(), operation.exceptionRefs().stream()).collect(Collectors.toList()), operation.graph().find(Schema.class));
	}
}
