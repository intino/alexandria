package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.jmx.JMXService.Operation;

import java.io.File;

public class JMXActionRenderer extends ActionRenderer {
	private final Operation operation;

	public JMXActionRenderer(Project project, Operation operation, File destiny, String packageName, String boxName) {
		super(project, destiny, packageName, boxName);
		this.operation = operation;
	}

	public void execute() {
		execute(operation.name$(), operation.response(), operation.parameterList(), operation.exceptionList(), operation.graph().schemaList());
	}
}
