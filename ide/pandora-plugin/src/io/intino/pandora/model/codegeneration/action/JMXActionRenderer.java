package io.intino.pandora.model.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.pandora.model.Schema;
import io.intino.pandora.model.jmx.JMXService.Operation;

import java.io.File;

public class JMXActionRenderer extends ActionRenderer {
	private final Operation operation;

	public JMXActionRenderer(Project project, Operation operation, File destiny, String packageName, String boxName) {
		super(project, destiny, packageName, boxName);
		this.operation = operation;
	}

	public void execute() {
		execute(operation.name(), operation.response(), operation.parameterList(), operation.exceptionList(), operation.graph().find(Schema.class));
	}
}
