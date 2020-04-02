package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Service;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceListRenderer extends UIRenderer {
	private final KonosGraph graph;

	public ServiceListRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext, Target.Accessor);
		this.graph = graph;
	}

	@Override
	public void render() {
		List<Service.UI> services = graph.serviceList(Service::isUI).map(Service::asUI).collect(Collectors.toList());
		services.forEach(s -> new ServiceCreator(context, s).execute());
	}
}
