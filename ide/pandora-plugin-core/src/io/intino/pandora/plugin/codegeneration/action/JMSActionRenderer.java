package io.intino.pandora.plugin.codegeneration.action;

import io.intino.pandora.plugin.Schema;
import io.intino.pandora.plugin.jms.JMSService.Resource;

import java.io.File;

public class JMSActionRenderer extends ActionRenderer {
	private final Resource resource;

	public JMSActionRenderer(Resource resource, File destiny, String packageName) {
		super(destiny, packageName);
		this.resource = resource;
	}

	public void execute() {
		execute(resource.name(), resource.response(), resource.parameterList(), resource.exceptionList(), resource.graph().find(Schema.class));
	}
}