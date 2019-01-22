package io.intino.konos.builder.codegeneration.services.ui.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.displays.renderers.DisplayRenderer;
import io.intino.konos.model.graph.Component;

@SuppressWarnings("Duplicates")
public class ComponentRenderer extends DisplayRenderer<Component> {

	public ComponentRenderer(Settings settings, Component component) {
		super(settings, component);
	}

}
