package io.intino.konos.builder.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.konos.model.graph.Schema;
import io.intino.konos.model.graph.jms.JMSService.Request;

import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JMSRequestActionRenderer extends ActionRenderer {
	private final Request request;

	public JMSRequestActionRenderer(Project project, Request request, File destiny, String packageName, String boxName) {
		super(project, destiny, packageName, boxName);
		this.request = request;
	}

	public void execute() {
		execute(request.name$(), request.response(), request.parameterList(),
				Stream.concat(request.exceptionList().stream(), request.exceptionRefs().stream()).collect(Collectors.toList()), request.graph().schemaList());
	}
}