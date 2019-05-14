package io.intino.konos.builder.codegeneration.accessor.ui;

import com.intellij.util.io.ZipUtil;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.accessor.ui.resource.ResourceListRenderer;
import io.intino.konos.builder.codegeneration.ui.I18nRenderer;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayListRenderer;
import io.intino.konos.model.graph.ui.UIService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class ServiceRenderer extends UIRenderer {
	private final UIService service;

	public ServiceRenderer(Settings settings, UIService service) {
		super(settings, Target.Accessor);
		this.service = service;
	}

	@Override
	public void render() {
		try {
			createSkeleton();
			new AppRenderer(settings, service).execute();
			new ThemeRenderer(settings, service).execute();
			new I18nRenderer(settings, service, target).execute();
			new DisplaysManifestRenderer(settings, service).execute();
			new DisplayListRenderer(settings, service, new AccessorTemplateProvider(), target).execute();
			new ResourceListRenderer(settings, service).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createSkeleton() throws IOException {
		final File destiny = accessorRes();
		final File file = new File(destiny, "ui.zip");
		copyResourcesRecursively(this.getClass().getResource("/ui/ui.zip"), file);
		ZipUtil.extract(file, destiny, null, false);
		file.delete();
		new File(destiny, "images").mkdirs();
	}

	private void copyResourcesRecursively(URL originUrl, File destination) {
		try {
			URLConnection urlConnection = originUrl.openConnection();
			if (urlConnection instanceof JarURLConnection)
				copyJarResourcesRecursively(destination, (JarURLConnection) urlConnection);
			else if (urlConnection instanceof FileURLConnection) {
				FileUtils.copyFile(new File(originUrl.getPath()), destination);
			} else throw new Exception("URLConnection[" + urlConnection.getClass().getSimpleName() +
					"] is not a recognized/implemented connection type.");
		} catch (Exception e) {
			LoggerFactory.getLogger(ROOT_LOGGER_NAME).error(e.getMessage(), e);
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
