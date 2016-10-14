package io.intino.pandora.plugin.codegeneration.action;

import io.intino.pandora.plugin.Schema;
import io.intino.pandora.plugin.rest.RESTService.Resource;

import java.io.File;

public class RESTActionRenderer extends ActionRenderer {
	private final Resource.Operation operation;

	public RESTActionRenderer(Resource.Operation operation, File destiny, String packageName) {
		super(destiny, packageName);
		this.operation = operation;
	}

	public void execute() {
		execute(firstUpperCase(operation.name()) + firstUpperCase(operation.owner().name()),
				operation.response(), operation.parameterList(), operation.exceptionList(), operation.graph().find(Schema.class));
	}
}
