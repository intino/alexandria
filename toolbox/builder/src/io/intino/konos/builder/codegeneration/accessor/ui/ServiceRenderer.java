package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.accessor.ui.resource.ResourceListRenderer;
import io.intino.konos.builder.codegeneration.ui.I18nRenderer;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayListRenderer;
import io.intino.konos.model.graph.Service;

public class ServiceRenderer extends UIRenderer {
	private final Service.UI service;

	public ServiceRenderer(Settings settings, Service.UI service) {
		super(settings, Target.Accessor);
		this.service = service;
	}

	@Override
	public void render() {
		new AppRenderer(settings, service).execute();
		new ThemeRenderer(settings, service).execute();
		new I18nRenderer(settings, service, target).execute();
		new DisplaysManifestRenderer(settings, service).execute();
		new DisplayListRenderer(settings, service, new AccessorTemplateProvider(), target).execute();
		new ResourceListRenderer(settings, service).execute();
	}

}
