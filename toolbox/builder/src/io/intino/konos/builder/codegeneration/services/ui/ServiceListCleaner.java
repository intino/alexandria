package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayListCleaner;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.ui.UIService;

public class ServiceListCleaner extends UIRenderer {
	private final KonosGraph graph;

	public ServiceListCleaner(Settings settings, KonosGraph graph) {
		super(settings, Target.Service);
		this.graph = graph;
	}

	@Override
	public void render() {
		graph.uIServiceList().forEach(this::cleanService);
	}

	private void cleanService(UIService service) {
		new DisplayListCleaner(settings, service).execute();
	}

}
