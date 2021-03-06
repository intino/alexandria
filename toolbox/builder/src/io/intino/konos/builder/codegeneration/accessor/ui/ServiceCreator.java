package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.zip.Zip;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.compiler.shared.PostCompileDependantWebModuleActionMessage;
import io.intino.konos.model.graph.Service;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class ServiceCreator extends UIRenderer {
	private final Service.UI service;

	public ServiceCreator(CompilationContext compilationContext, Service.UI service) {
		super(compilationContext, Target.Accessor);
		this.service = service;
	}

	@Override
	public void render() throws KonosException {
		try {
			context.postCompileActionMessages().add(new PostCompileDependantWebModuleActionMessage(context.configuration().module(), service.name$()));
			if (context.webModuleDirectory() == null)
				context.webModuleDirectory(new File(context.configuration().moduleDirectory().getParentFile(), Formatters.camelCaseToSnakeCase().format(service.name$()).toString()));
			if (!context.webModuleDirectory().exists()) createSkeleton();
			new ServiceRenderer(context, service).execute();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void createSkeleton() throws IOException {
		final File destiny = root();
		final File file = new File(destiny, "ui.zip");
		copyResourcesRecursively(this.getClass().getResource("/ui/ui.zip"), file);
		new Zip(file).unzip(destiny.getAbsolutePath());
		file.delete();
		new File(destiny, "images").mkdirs();
	}

	private void copyResourcesRecursively(URL originUrl, File destination) {
		try {
			URLConnection urlConnection = originUrl.openConnection();
			if (urlConnection instanceof JarURLConnection)
				copyJarResourcesRecursively(destination, (JarURLConnection) urlConnection);
			else FileUtils.copyFile(new File(originUrl.getPath()), destination);
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	private void copyJarResourcesRecursively(File destination, JarURLConnection jarConnection) throws IOException {
		JarFile jarFile = jarConnection.getJarFile();
		for (Enumeration list = jarFile.entries(); list.hasMoreElements(); ) {
			JarEntry entry = (JarEntry) list.nextElement();
			if (entry.getName().startsWith(jarConnection.getEntryName())) {
				String fileName = StringUtils.removeStart(entry.getName(), jarConnection.getEntryName());
				if (!entry.isDirectory()) try (InputStream entryInputStream = jarFile.getInputStream(entry)) {
					FileUtils.copyInputStreamToFile(entryInputStream, new File(destination, fileName));
				}
				else new File(destination, fileName).exists();
			}
		}
	}
}
