package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.konos.builder.codegeneration.Cleaner;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayListCleaner;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.ui.UIService;

public class ServiceListCleaner extends Cleaner {
	private final KonosGraph graph;

	public ServiceListCleaner(Settings settings, KonosGraph graph) {
		super(settings);
		this.graph = graph;
	}

	@Override
	public void execute() {
		graph.uIServiceList().forEach(this::cleanService);
	}

	private void cleanService(UIService service) {
		new DisplayListCleaner(settings, service).execute();
	}

}
