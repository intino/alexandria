package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.KonosGraph;

public class ServiceListRenderer extends UIRenderer {
	private final KonosGraph graph;

	public ServiceListRenderer(Settings settings, KonosGraph graph) {
		super(settings, Target.Accessor);
		this.graph = graph;
	}

	@Override
	public void render() {
		graph.uIServiceList().forEach(s -> new ServiceCreator(settings, s).execute());
	}

}
