package io.intino.konos.builder.codegeneration.accessor.ui.accessible;

import io.intino.alexandria.logger.Logger;
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
import java.io.IOException;
import java.nio.file.Files;

public class UiAccessibleAccessorRenderer extends Renderer {
	private final Service.UI service;
	private final File destination;

	public UiAccessibleAccessorRenderer(CompilationContext compilationContext, Service.UI service, File destination) {
		super(compilationContext);
		this.service = service;
		this.destination = destination;
		this.destination.mkdirs();
	}

	@Override
	public void render() throws KonosException {
		try {
			context.serviceDirectory(new File(context.configuration().moduleDirectory().getParentFile(), Formatters.camelCaseToKebabCase().format(service.name$()).toString()));
			new ServiceRenderer(context, service, Target.AccessibleAccessor, destination()).execute();
			new DisplayListRenderer(context, service, new AccessibleRendererWriter(context, destination())).execute();
			new I18nRenderer(context, service, Target.AccessibleAccessor, destination()).execute();
			Files.createDirectory(new File(destination, "items").toPath());
			Files.createDirectory(new File(destination, "items").toPath());
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private File destination() {
		return new File(destination, packageName().replace(".", File.separator));
	}

}
