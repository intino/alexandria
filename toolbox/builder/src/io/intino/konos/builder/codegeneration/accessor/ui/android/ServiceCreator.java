package io.intino.konos.builder.codegeneration.accessor.ui.android;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.zip.Zip;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.utils.FileHelper;
import io.intino.konos.model.Service;

import java.io.File;
import java.io.IOException;

public class ServiceCreator extends UIRenderer {
	private final Service.UI service;
	private File serviceDirectory;

	public ServiceCreator(CompilationContext compilationContext, Service.UI service, File serviceDirectory) {
		super(compilationContext);
		this.service = service;
		this.serviceDirectory = serviceDirectory;
	}

	@Override
	public void render() throws KonosException {
		try {
			context.serviceDirectory(serviceDirectory);
			if (!context.serviceDirectory().exists()) createSkeleton();
			new ServiceRenderer(context, service).execute();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void createSkeleton() throws IOException {
		final File destiny = serviceDirectory;
		final File file = new File(destiny, "mobile.zip");
		FileHelper.copyResourcesRecursively(this.getClass().getResource("/ui/mobile.zip"), file);
		new Zip(file).unzip(destiny.getAbsolutePath());
		file.delete();
	}

}
