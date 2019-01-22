package io.intino.konos.builder.codegeneration.services.ui.displays;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.UIRenderer;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.KonosGraph;

import java.util.List;

@SuppressWarnings("Duplicates")
public class DisplayListRenderer extends UIRenderer {
	private final List<Display> displays;

	public DisplayListRenderer(Settings settings, KonosGraph graph) {
		super(settings);
		this.displays = graph.displayList();
	}

	public void execute() {
		DisplayRendererFactory factory = new DisplayRendererFactory();
		displays.forEach(d -> factory.renderer(settings, d).execute());
	}

}