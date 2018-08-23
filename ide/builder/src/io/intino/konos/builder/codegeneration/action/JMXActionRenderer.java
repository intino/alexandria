package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.jmx.JMXService.Operation;

import java.io.File;
import java.util.Map;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class JMXActionRenderer extends ActionRenderer {
	private final Operation operation;
	private final Map<String, String> classes;

	public JMXActionRenderer(Project project, Operation operation, File destiny, String packageName, String boxName, Map<String, String> classes) {
		super(project, destiny, packageName, boxName, "jmx");
		this.operation = operation;
		this.classes = classes;
	}

	public void execute() {
		classes.put(operation.getClass().getSimpleName() + "#" + firstUpperCase(operation.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(operation.name$())) + "Action");
		execute(operation.name$(), operation.response(), operation.parameterList(), operation.exceptionList(), operation.graph().schemaList());
	}
}
