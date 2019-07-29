package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.cache.ElementCache;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.PrivateComponents;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.magritte.Layer;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;

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

	protected Configuration configuration() {
		return module() != null ? TaraUtil.configurationOf(module()) : null;
	}

	protected String packageName() {
		return settings.packageName();
	}

	protected Module module() {
		return settings.module();
	}

	protected String parent() {
		return settings.parent();
	}

	protected File root() {
		return settings.root(target);
	}

	protected File res() {
		return settings.res(target);
	}

	protected File src() {
		return settings.src(target);
	}

	protected File gen() {
		return settings.gen(target);
	}

	protected Map<String, String> classes() {
		return settings.classes();
	}

	protected ElementCache cache() {
		return settings.cache();
	}

	public FrameBuilder buildBaseFrame() {
		return new FrameBuilder().add("box", boxName()).add("package", settings.packageName());
	}

	protected boolean isRendered(Layer element) {
		if (element == null) return false;
		if (element.i$(CatalogComponents.Collection.Mold.Item.class)) return false;
		if (element.i$(PrivateComponents.Row.class)) return false;
		return !cache().isDirty(element);
	}

	protected boolean isRoot(Layer element) {
		return element.core$().owner() == null || element.core$().owner() == element.core$().model();
	}

	protected void saveRendered(Layer element) {
		settings.cache().add(element);
	}

}
