package io.intino.konos.builder.codegeneration.accessor.ui.web;

import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.dsl.KonosGraph;
import io.intino.konos.dsl.Service;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceListRenderer extends UIRenderer {
	private final KonosGraph graph;

	public ServiceListRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext);
		this.graph = graph;
	}

	@Override
	public void render() throws KonosException {
		List<Service.UI> services = graph.serviceList(this::isWeb).map(Service::asUI).collect(Collectors.toList());
		for (Service.UI s : services) {
			ComponentRenderer.clearCache();
			new ServiceCreator(context, s).execute();
		}
	}

	private boolean isWeb(Service service) {
		return service.isUI() && service.asUI().targets().contains(Service.UI.Targets.Web);
	}
}
