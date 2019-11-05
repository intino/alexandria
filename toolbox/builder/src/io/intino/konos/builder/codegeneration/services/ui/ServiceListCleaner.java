package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.konos.builder.codegeneration.Cleaner;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayListCleaner;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Service;

public class ServiceListCleaner extends Cleaner {
	private final KonosGraph graph;

	public ServiceListCleaner(Settings settings, KonosGraph graph) {
		super(settings);
		this.graph = graph;
	}

	@Override
	public void execute() {
		graph.serviceList().stream().filter(Service::isUI).map(Service::asUI).forEach(this::cleanService);
	}

	private void cleanService(Service.UI service) {
		if (settings.webModule() != null) clean(gen(Target.Accessor));
		new DisplayListCleaner(settings, service).execute();
	}
}
