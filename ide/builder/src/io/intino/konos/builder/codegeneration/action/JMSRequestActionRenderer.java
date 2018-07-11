package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.jms.JMSService.Request;

import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class JMSRequestActionRenderer extends ActionRenderer {
	private final Request request;
	private final Map<String, String> classes;

	public JMSRequestActionRenderer(Project project, Request request, File destiny, String packageName, String boxName, Map<String, String> classes) {
		super(project, destiny, packageName, boxName);
		this.request = request;
		this.classes = classes;
	}

	public void execute() {
		classes.put(request.getClass().getSimpleName() + "#" + firstUpperCase(request.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(request.name$())) + "Action");
		execute(request.name$(), request.response(), request.parameterList(),
				Stream.concat(request.exceptionList().stream(), request.exceptionRefs().stream()).collect(Collectors.toList()), request.graph().schemaList());
	}
}