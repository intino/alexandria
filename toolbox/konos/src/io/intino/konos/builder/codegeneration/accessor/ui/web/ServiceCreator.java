package io.intino.konos.builder.codegeneration.accessor.ui.web;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.zip.Zip;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.utils.FileHelper;
import io.intino.builder.PostCompileDependantWebModuleActionMessage;
import io.intino.konos.dsl.Service;

import java.io.File;
import java.io.IOException;


public class ServiceCreator extends UIRenderer {
	private final Service.UI service;

	public ServiceCreator(CompilationContext compilationContext, Service.UI service) {
		super(compilationContext);
		this.service = service;
	}

	@Override
	public void render() throws KonosException {
		try {
			context.postCompileActionMessages().add(new PostCompileDependantWebModuleActionMessage(context.configuration().module(), service.name$()));
			context.serviceDirectory(new File(context.configuration().moduleDirectory().getParentFile(), Formatters.camelCaseToSnakeCase().format(service.name$()).toString()));
			if (!context.serviceDirectory().exists()) createSkeleton();
			new ServiceRenderer(context, service).execute();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void createSkeleton() throws IOException {
		final File destiny = root(Target.Accessor);
		final File file = new File(destiny, "web.zip");
		FileHelper.copyResourcesRecursively(this.getClass().getResource("/ui/web.zip"), file);
		new Zip(file).unzip(destiny.getAbsolutePath());
		file.delete();
		new File(destiny, "images").mkdirs();
	}

}