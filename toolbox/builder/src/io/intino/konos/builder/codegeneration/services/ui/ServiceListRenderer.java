package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayListRenderer;
import io.intino.konos.builder.codegeneration.ui.resource.ResourceListRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Service;

import java.io.File;

import static java.util.stream.Collectors.toList;

public class ServiceListRenderer extends UIRenderer {
	private final KonosGraph graph;

	public ServiceListRenderer(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext, Target.Owner);
		this.graph = graph;
	}

	@Override
	public void render() throws KonosException {
		for (Service service : graph.serviceList(Service::isUI).collect(toList())) processUIService(service.asUI());
		new ResourceListRenderer(context, graph, Target.Owner).execute();
	}

	private void processUIService(Service.UI service) throws KonosException {
		context.webModuleDirectory(new File(context.configuration().moduleDirectory().getParentFile(), Formatters.camelCaseToSnakeCase().format(service.name$()).toString()));
		new ServiceRenderer(context, service).execute();
		new DisplayListRenderer(context, service, templateProvider(), target).execute();
	}

	private TemplateProvider templateProvider() {
		return new ServiceTemplateProvider();
	}

}
