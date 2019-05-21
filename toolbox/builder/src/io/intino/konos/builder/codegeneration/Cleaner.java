package io.intino.konos.builder.codegeneration;

import io.intino.konos.builder.codegeneration.cache.ElementCache;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.tara.magritte.Layer;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.fileOf;

public abstract class Cleaner {
	protected final ElementHelper elementHelper;
	protected final Settings settings;

	protected static final List<String> ExcludedDirectories = Arrays.asList("displays", ".cache");

	public Cleaner(Settings settings) {
		this.settings = settings;
		this.elementHelper = new ElementHelper();
	}

	public abstract void execute();

	protected File res(Target target) {
		return settings.res(target);
	}

	protected File src(Target target) {
		return settings.src(target);
	}

	protected File gen(Target target) {
		return settings.gen(target);
	}

	protected ElementCache cache() {
		return settings.cache();
	}

	protected String typeOf(Layer element) {
		return elementHelper.typeOf(element);
	}

	protected String nameOf(Layer element) {
		return elementHelper.nameOf(element);
	}

	protected void remove(File folder, String name, Target target) {
		File file = fileOf(folder, snakeCaseToCamelCase(name), target);
		if (file.exists()) file.delete();
	}

	protected void clean(File directory) {
		if (!directory.exists()) return;
		List<File> files = Arrays.asList(directory.listFiles(pathname -> !ExcludedDirectories.contains(pathname.getName())));
		files.forEach(f -> {
			if (f.isDirectory()) clean(f);
			f.delete();
		});
	}
}
