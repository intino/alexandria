package io.intino.konos.builder.codegeneration.accessor.ui.exposed;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.services.ui.ServiceRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.I18nRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayListRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.dsl.Service;

import java.io.File;

public class UiExposedAccessorRenderer extends Renderer {
	private final Service.UI service;
	private final File destination;

	public UiExposedAccessorRenderer(CompilationContext compilationContext, Service.UI service, File destination) {
		super(compilationContext);
		this.service = service;
		this.destination = destination;
		this.destination.mkdirs();
	}

	@Override
	public void render() throws KonosException {
		context.serviceDirectory(new File(context.configuration().moduleDirectory().getParentFile(), Formatters.camelCaseToKebabCase().format(service.name$()).toString()));
		new ServiceRenderer(context, service, Target.ExposedAccessor, destination()).execute();
		new DisplayListRenderer(context, service, new ExposedRendererWriter(context, destination())).execute();
		new I18nRenderer(context, service, Target.ExposedAccessor, destination()).execute();
	}

	private File destination() {
		return new File(destination, packageName().replace(".", File.separator));
	}

}
