package io.intino.konos.builder.codegeneration.services.ui;

public enum Target {
	Server, Accessor, MobileShared, Android, AndroidResource;

	public boolean requirePackageName() {
		return this == Server || this == MobileShared || this == Android;
	}
}

