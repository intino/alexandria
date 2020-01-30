package io.intino.konos.compiler.shared;


public class PostCompileDependantWebModuleActionMessage extends PostCompileActionMessage {
	public PostCompileDependantWebModuleActionMessage(String module, String webModuleName) {
		super(module, null, ObjectType.MODULE, webModuleName);
	}
}
