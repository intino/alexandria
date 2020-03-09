package io.intino.konos.compiler.shared;

import java.io.File;

import static io.intino.konos.compiler.shared.KonosBuildConstants.SEPARATOR;

public abstract class PostCompileActionMessage {
	protected final String module;
	protected final File file;
	protected final ObjectType objectType;
	protected final String name;

	public enum ObjectType {
		FIELD, METHOD, CONFIGURATION_PARAMETER, CONFIGURATION_DEPENDENCY, MODULE
	}

	public PostCompileActionMessage(String module, File file, ObjectType objectType, String name) {
		this.module = module;
		this.file = file;
		this.objectType = objectType;
		this.name = name;
	}

	@Override
	public String toString() {
		return module + SEPARATOR + objectType.name() + SEPARATOR + file + SEPARATOR + name;
	}
}
