package io.intino.konos.builder.codegeneration.accessor.ui.android;

import io.intino.konos.builder.codegeneration.accessor.ui.android.resource.ResourceListRenderer;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayListRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.dsl.Component;
import io.intino.konos.dsl.PassiveView;
import io.intino.konos.dsl.Service;
import io.intino.magritte.framework.Layer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ServiceRenderer extends UIRenderer {
	private final Service.UI service;

	public ServiceRenderer(CompilationContext compilationContext, Service.UI service) {
		super(compilationContext);
		this.service = service;
	}

	@Override
	public void render() throws KonosException {
		new DisplayListRenderer(context, service, new AndroidRendererWriter(context)).execute();
		new DisplaysManifestRenderer(context, service).execute();
		new ResourceListRenderer(context, service).execute();
		new ThemeRenderer(context, service, usedFormats()).execute();
	}

	private static final Set<String> FormatSet = Collections.synchronizedSet(new HashSet<>());

	private Set<String> usedFormats() {
		service.graph().rootDisplays(context.graphName()).forEach(this::registerFormats);
		return ServiceRenderer.FormatSet;
	}

	private void registerFormats(PassiveView display) {
		components(display).forEach(this::registerFormats);
		if (!display.i$(Component.class)) return;
		Component component = display.a$(Component.class);
		if (component.format() == null) return;
		String[] format = component.format().stream().map(Layer::name$).sorted().toArray(String[]::new);
		if (format.length == 0) return;
		ServiceRenderer.FormatSet.add(String.join("-", format));
	}

}
