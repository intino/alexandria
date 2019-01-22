package io.intino.konos.builder.codegeneration.services.ui.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.displays.updaters.DisplayUpdater;
import io.intino.konos.model.graph.Component;

import java.io.File;

public class ComponentUpdater extends DisplayUpdater<Component> {

	public ComponentUpdater(Settings settings, Component component, File file) {
		super(settings, component, file);
	}

}
