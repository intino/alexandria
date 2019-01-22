package io.intino.konos.builder.codegeneration.services.ui.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.UIRenderer;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.KonosGraph;

import java.util.List;

@SuppressWarnings("Duplicates")
public class ComponentListRenderer extends UIRenderer {
	private final List<Component> components;

	public ComponentListRenderer(Settings settings, KonosGraph graph) {
		super(settings);
		this.components = graph.componentList();
	}

	public void execute() {
		components.forEach(d -> new ComponentRenderer(settings, d).execute());
	}

}