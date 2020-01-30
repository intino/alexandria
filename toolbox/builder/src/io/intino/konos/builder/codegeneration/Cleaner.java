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
	protected final CompilationContext compilationContext;

	protected static final List<String> ExcludedDirectories = Arrays.asList("displays", "graph", ".cache");

	public Cleaner(CompilationContext compilationContext) {
		this.compilationContext = compilationContext;
		this.elementHelper = new ElementHelper();
	}

	public abstract void execute();

	protected File res(Target target) {
		return compilationContext.res(target);
	}

	protected File src(Target target) {
		return compilationContext.src(target);
	}

	protected File gen(Target target) {
		return compilationContext.gen(target);
	}

	protected ElementCache cache() {
		return compilationContext.cache();
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
