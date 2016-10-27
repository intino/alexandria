package io.intino.pandora.plugin.codegeneration.action;

import com.intellij.openapi.project.Project;
import io.intino.pandora.plugin.Schema;
import io.intino.pandora.plugin.jms.JMSService.Request;

import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JMSRequestActionRenderer extends ActionRenderer {
	private final Request request;

	public JMSRequestActionRenderer(Project project, Request request, File destiny, String packageName) {
		super(project,destiny, packageName);
		this.request = request;
	}

	public void execute() {
		execute(request.name(), request.response(), request.parameterList(),
				Stream.concat(request.exceptionList().stream(), request.exceptionRefs().stream()).collect(Collectors.toList()), request.graph().find(Schema.class));
	}
}