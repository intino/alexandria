package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayListRenderer;
import io.intino.konos.builder.codegeneration.ui.resource.ResourceListRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.ui.UIService;

public class ServiceListRenderer extends UIRenderer {
	private final KonosGraph graph;

	public ServiceListRenderer(Settings settings, KonosGraph graph) {
		super(settings, Target.Service);
		this.graph = graph;
	}

	public void execute() {
		graph.uIServiceList().forEach(this::processUIService);
		new DisplayListRenderer(settings, graph, templateProvider(), target).execute();
		new ResourceListRenderer(settings, graph, Target.Service).execute();
	}

	private void processUIService(UIService service) {
		new ServiceRenderer(settings, service).execute();
	}

	private TemplateProvider templateProvider() {
		return new ServiceTemplateProvider();
	}

}
