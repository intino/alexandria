package io.intino.konos.builder.codegeneration.accessor.ui.android;

import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.RouteDispatcherRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.dsl.KonosGraph;
import io.intino.konos.dsl.Service;

import java.io.File;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ServiceListRenderer extends UIRenderer {
	private final KonosGraph graph;
	private final Function<Service.UI, File> genDirectoryProvider;

	public ServiceListRenderer(CompilationContext compilationContext, KonosGraph graph, Function<Service.UI, File> genDirectoryProvider) {
		super(compilationContext);
		this.graph = graph;
		this.genDirectoryProvider = genDirectoryProvider;
	}

	@Override
	public void render() throws KonosException {
		List<Service.UI> services = graph.serviceList(graph::isAndroid).map(Service::asUI).collect(Collectors.toList());
		for (Service.UI s : services) new ServiceCreator(context, s, genDirectoryProvider.apply(s)).execute();
		if (services.isEmpty()) return;
		new AppRenderer(context, services).execute();
		new RouteDispatcherRenderer(context, services, Target.Android).execute();
	}
}