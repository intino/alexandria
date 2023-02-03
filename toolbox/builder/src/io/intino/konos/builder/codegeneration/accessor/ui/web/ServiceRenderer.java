package io.intino.konos.builder.codegeneration.accessor.ui.web;

import io.intino.konos.builder.codegeneration.accessor.ui.web.resource.ResourceListRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.I18nRenderer;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayListRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.model.Service;

public class ServiceRenderer extends UIRenderer {
	private final Service.UI service;

	public ServiceRenderer(CompilationContext compilationContext, Service.UI service) {
		super(compilationContext);
		this.service = service;
	}

	@Override
	public void render() throws KonosException {
		new AppRenderer(context, service).execute();
		new ThemeRenderer(context, service).execute();
		new I18nRenderer(context, service, Target.Accessor).execute();
		new DisplaysManifestRenderer(context, service).execute();
		new DisplayListRenderer(context, service, new WebRendererWriter(context)).execute();
		new ResourceListRenderer(context, service).execute();
	}

}
