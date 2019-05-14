package io.intino.konos.builder.codegeneration;

import io.intino.konos.builder.codegeneration.cache.ElementCache;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.tara.magritte.Layer;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;

public abstract class Cleaner {
	private final ElementHelper elementHelper;
	protected final Settings settings;

	public Cleaner(Settings settings) {
		this.settings = settings;
		this.elementHelper = new ElementHelper();
	}

	public abstract void execute();

	protected File res() {
		return settings.res();
	}

	protected File src() {
		return settings.src();
	}

	protected File gen() {
		return settings.gen();
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

	protected void remove(File folder, String name) {
		File file = javaFile(folder, snakeCaseToCamelCase(name));
		if (file.exists()) file.delete();
	}
}
