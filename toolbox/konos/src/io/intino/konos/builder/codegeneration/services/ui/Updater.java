package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.konos.builder.context.CompilationContext;
import io.intino.builder.PostCompileMethodActionMessage;

import java.io.File;
import java.util.Collections;

public abstract class Updater {
	protected final CompilationContext compilationContext;
	protected File file;

	public Updater(CompilationContext compilationContext, File file) {
		this.compilationContext = compilationContext;
		this.file = file;
	}

	public abstract void update();


	protected void createMethod(String name, String returnType) {
		compilationContext.postCompileActionMessages().add(new PostCompileMethodActionMessage(compilationContext.module(), file, name, true, Collections.emptyList(), returnType));
	}
}
