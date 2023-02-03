package io.intino.konos.builder.codegeneration.accessor.ui.android;

import io.intino.konos.builder.codegeneration.accessor.ui.android.resource.ResourceListRenderer;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayListRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
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
		new DisplayListRenderer(context, service, new AndroidRendererWriter(context)).execute();
		new ResourceListRenderer(context, service).execute();
		new ThemeRenderer(context, service, ComponentRenderer.formatSet).execute();
	}

}
