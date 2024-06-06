package io.intino.konos.builder.codegeneration.services.ui;

public enum Target {
	Service, Accessor, AccessibleAccessor, MobileShared, Android, AndroidResource;

	public boolean requirePackageName() {
		return this == Service || this == MobileShared || this == Android;
	}
}

