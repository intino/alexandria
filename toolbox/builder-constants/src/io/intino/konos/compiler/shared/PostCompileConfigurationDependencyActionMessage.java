package io.intino.konos.compiler.shared;

public class PostCompileConfigurationDependencyActionMessage extends PostCompileActionMessage {

	public PostCompileConfigurationDependencyActionMessage(String module, String identifier) {
		super(module, null, ObjectType.CONFIGURATION_DEPENDENCY, identifier);
	}
}
