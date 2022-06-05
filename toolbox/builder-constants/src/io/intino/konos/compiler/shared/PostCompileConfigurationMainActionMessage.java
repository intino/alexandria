package io.intino.konos.compiler.shared;

public class PostCompileConfigurationMainActionMessage extends PostCompileActionMessage {

	public PostCompileConfigurationMainActionMessage(String module, String qualifiedName) {
		super(module, null, ObjectType.MAIN_CLASS, qualifiedName);
	}
}
