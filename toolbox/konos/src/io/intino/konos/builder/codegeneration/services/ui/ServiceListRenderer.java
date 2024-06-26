package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayListRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.RouteDispatcherRenderer;
import io.intino.konos.builder.codegeneration.ui.resource.ResourceListRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.dsl.KonosGraph;
import io.intino.konos.dsl.Service;

import java.io.File;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ServiceListRenderer extends UIRenderer {
	private final KonosGraph graph;

	public ServiceListRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext);
		this.graph = graph;
	}

	@Override
	public void render() throws KonosException {
		List<Service.UI> uiServices = graph.serviceList(Service::isUI).map(Service::asUI).collect(toList());
		for (Service.UI service : uiServices) processUIService(service);
		new ResourceListRenderer(context, graph, Target.Service).execute();
		if (!uiServices.isEmpty()) new RouteDispatcherRenderer(context, uiServices, Target.Service).execute();
	}

	private void processUIService(Service.UI service) throws KonosException {
		context.serviceDirectory(new File(context.configuration().moduleDirectory().getParentFile(), Formatters.camelCaseToKebabCase().format(service.name$()).toString()));
		new ServiceRenderer(context, service, Target.Service).execute();
		new DisplayListRenderer(context, service, writer()).execute();
	}

	private RendererWriter writer() {
		return new ServerRendererWriter(context);
	}

}
