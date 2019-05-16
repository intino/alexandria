package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.cache.ElementCache;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.tara.magritte.Layer;

import java.io.File;
import java.util.Map;

public abstract class Renderer {
	protected final Settings settings;
	protected final ElementHelper elementHelper;
	protected final Target target;

	protected Renderer(Settings settings, Target target) {
		this.settings = settings;
		this.elementHelper = new ElementHelper();
		this.target = target;
	}

	public void execute() {
		render();
	}

	protected abstract void render();

	public Project project() {
		return settings.project();
	}

	public String boxName() {
		return settings.boxName();
	}

	protected String packageName() {
		return settings.packageName();
	}

	protected File res() {
		return createIfNotExists((target == Target.Service) ? settings.res() : accessorSrc());
	}

	protected File accessorRes() {
		return createIfNotExists(new File(accessorRoot() + File.separator + "res"));
	}

	protected File src() {
		return createIfNotExists((target == Target.Service) ? settings.src() : accessorSrc());
	}

	protected File accessorSrc() {
		return createIfNotExists(new File(accessorRoot() + File.separator + "src"));
	}

	protected File gen() {
		return createIfNotExists((target == Target.Service) ? settings.gen() : accessorGen());
	}

	protected File accessorGen() {
		return createIfNotExists(new File(accessorRoot() + File.separator + "gen"));
	}

	protected Module module() {
		return settings.module();
	}

	protected String parent() {
		return settings.parent();
	}

	protected File accessorRoot() {
		return createIfNotExists(new File(settings.webModule().getModuleFilePath()).getParentFile());
	}

	protected Map<String, String> classes() {
		return settings.classes();
	}

	protected ElementCache cache() {
		return settings.cache();
	}

	public FrameBuilder baseFrameBuilder() {
		return new FrameBuilder().add("box", boxName()).add("package", settings.packageName());
	}

	protected boolean isRendered(Layer element) {
		if (element == null) return false;
		String key = elementHelper.referenceOf(element).toString();
		if (element.name$().contains("testTemplate") && element.core$().birthMark() != -1273592974)
			System.out.println("Fallo!");
		boolean containsKey = settings.cache().containsKey(key);
		return containsKey && settings.cache().get(key).equals((long) element.core$().birthMark());
	}

	protected void saveRendered(Layer element) {
		settings.cache().add(element);
	}

	protected File createIfNotExists(File file) {
		if (!file.exists()) file.mkdirs();
		return file;
	}
}
