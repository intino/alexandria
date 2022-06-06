package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.ui.displays.DisplayUpdater;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.Component;

import java.io.File;

public class ComponentUpdater extends DisplayUpdater<Component> {

	public ComponentUpdater(CompilationContext compilationContext, Component component, File file) {
		super(compilationContext, component, file);
	}

}
