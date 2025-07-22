package io.intino.alexandria.ui.services.libraries;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.utils.ZipHelper;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class AlexandriaLibraryLoader extends ClassLoader {
	private final AlexandriaLibraryArchetype archetype;

	private static final Map<String, Class<?>> Classes = new HashMap<>();

	public AlexandriaLibraryLoader(AlexandriaLibraryArchetype archetype) {
		super();
		this.archetype = archetype;
		extractClasses();
	}

	byte[] getClassBytes(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedInputStream bis = new BufferedInputStream(is);
		boolean eof = false;
		while (!eof) {
			try {
				int i = bis.read();
				if (i == -1)
					eof = true;
				else baos.write(i);
			} catch (IOException e) {
				Logger.error(e);
				return null;
			}
		}
		return baos.toByteArray();
	}

	private static final String I18nClass = "%s.I18n";
	public Class<?> i18nClass() {
		try {
			return loadClass(String.format(I18nClass, archetype.rootPackage(), archetype.libraryName()));
		} catch (ClassNotFoundException e) {
			Logger.error(e);
			return null;
		}
	}

	private static final String ServiceClass = "%s.ui.%sElementsService";
	public Class<?> serviceClass() {
		try {
			return loadClass(String.format(ServiceClass, archetype.rootPackage(), archetype.libraryName()));
		} catch (ClassNotFoundException e) {
			Logger.error(e);
			return null;
		}
	}

	private static final String TemplateClass = "%s.ui.resources.%sResource";
	public Class<?> libraryTemplateEntryPointClass(String name) {
		try {
			return loadClass(String.format(TemplateClass, archetype.rootPackage(), firstUpperCase(name)));
		} catch (ClassNotFoundException e) {
			Logger.error(e);
			return null;
		}
	}

	public File webDirectory() {
		return archetype.webDirectory();
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		byte[] buf;
		Class<?> resultClass;
		File file;
		InputStream is = null;

		try {

			if (AlexandriaLibraryLoader.Classes.containsKey(name))
				return AlexandriaLibraryLoader.Classes.get(name);

			file = new File(basePath(), name.replace(".", "/") + ".class");

			if (!file.exists())
				return this.getClass().getClassLoader().loadClass(name);

			is = new FileInputStream(file);
			buf = getClassBytes(is);
			resultClass = defineClass(name, buf, 0, buf.length, null);

			if (isLayoutsClass(name)) AlexandriaLibraryLoader.Classes.put(name, resultClass);

			return resultClass;
		} catch (ClassNotFoundException ignored) {
		} catch (FileNotFoundException e) {
			Logger.error(e);
		} finally {
			try { if (is != null) is.close(); } catch (Exception ignored) {}
		}
		return null;
	}

	@Override
	protected URL findResource(String name) {

		try {
			File file = new File(basePath(), name);

			if (!file.exists())
				return super.findResource(name);

			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			return null;
		}

	}

	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		Enumeration<URL> superResources = super.findResources(name);
		ArrayList<URL> result = new ArrayList<URL>();

		while (superResources.hasMoreElements())
			result.add(superResources.nextElement());

		File file = new File(basePath(), name);
		if (file.exists())
			result.add(file.toURI().toURL());

		return Collections.enumeration(result);
	}

	private boolean isLayoutsClass(String name) {
		return new File(basePath(), name.replace(".", "/") + ".class").exists();
	}

	public static void reset() {
		AlexandriaLibraryLoader.Classes.clear();
	}

	private void extractClasses() {
		File[] files = archetype.directory().listFiles();
		if (files != null && files.length > 0) return;
		if (!archetype.directory().exists()) archetype.directory().mkdirs();
		if (!archetype.file().exists()) return;
		ZipHelper.extract(archetype.file(), archetype.directory());
	}

	private File basePath() {
		return archetype.directory();
	}

	public static String firstUpperCase(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

}