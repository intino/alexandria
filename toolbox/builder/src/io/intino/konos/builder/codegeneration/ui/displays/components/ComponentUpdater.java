package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayUpdater;
import io.intino.konos.model.graph.Component;

import java.io.File;

public class ComponentUpdater extends DisplayUpdater<Component> {

	public ComponentUpdater(CompilationContext compilationContext, Component component, File file) {
		super(compilationContext, component, file);
	}

}
