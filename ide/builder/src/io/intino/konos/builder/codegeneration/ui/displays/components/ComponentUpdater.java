package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayUpdater;
import io.intino.konos.model.graph.Components.Component;

import java.io.File;

public class ComponentUpdater extends DisplayUpdater<Component> {

	public ComponentUpdater(Settings settings, Component component, File file) {
		super(settings, component, file);
	}

}
