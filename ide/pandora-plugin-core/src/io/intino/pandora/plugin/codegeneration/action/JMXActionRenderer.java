package io.intino.pandora.plugin.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.pandora.plugin.Schema;
import io.intino.pandora.plugin.jmx.JMXService.Operation;

import java.io.File;

public class JMXActionRenderer extends ActionRenderer {
	private final Operation operation;

	public JMXActionRenderer(Project project, Operation operation, File destiny, String packageName) {
		super(project, destiny, packageName);
		this.operation = operation;
	}

	public void execute() {
		execute(operation.name(), operation.response(), operation.parameterList(), operation.exceptionList(), operation.graph().find(Schema.class));
	}
}
