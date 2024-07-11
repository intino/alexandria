package io.intino.konos.builder.codegeneration.accessor.ui.android;

import io.intino.konos.builder.codegeneration.accessor.ui.android.resource.ResourceListRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayListRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.CodeGenerationHelper;
import io.intino.konos.dsl.*;
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
		createEssentialDirs();
		new DisplayListRenderer(context, service, new AndroidRendererWriter(context)).execute();
		new DisplaysManifestRenderer(context, service).execute();
		new ResourceListRenderer(context, service).execute();
		new ThemeRenderer(context, service, usedFormats()).execute();
	}

	private void createEssentialDirs() {
		CodeGenerationHelper.displaysFolder(src(Target.Android), Component.class.getSimpleName(), Target.Android).mkdirs();
		CodeGenerationHelper.displaysFolder(src(Target.Android), HelperComponents.Row.class.getSimpleName(), Target.Android).mkdirs();
		CodeGenerationHelper.displaysFolder(src(Target.Android), CatalogComponents.Moldable.Mold.Item.class.getSimpleName(), Target.Android).mkdirs();
		CodeGenerationHelper.displayRequestersFolder(src(Target.MobileShared), Target.Android).mkdirs();
		CodeGenerationHelper.displayNotifiersFolder(src(Target.MobileShared), Target.Android).mkdirs();
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
