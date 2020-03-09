package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.accessor.ui.resource.ResourceListRenderer;
import io.intino.konos.builder.codegeneration.ui.I18nRenderer;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayListRenderer;
import io.intino.konos.model.graph.Service;

public class ServiceRenderer extends UIRenderer {
	private final Service.UI service;

	public ServiceRenderer(CompilationContext compilationContext, Service.UI service) {
		super(compilationContext, Target.Accessor);
		this.service = service;
	}

	@Override
	public void render() {
		new AppRenderer(context, service).execute();
		new ThemeRenderer(context, service).execute();
		new I18nRenderer(context, service, target).execute();
		new DisplaysManifestRenderer(context, service).execute();
		new DisplayListRenderer(context, service, new AccessorTemplateProvider(), target).execute();
		new ResourceListRenderer(context, service).execute();
	}

}
