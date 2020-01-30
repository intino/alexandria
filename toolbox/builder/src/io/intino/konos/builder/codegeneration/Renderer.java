package io.intino.konos.builder.codegeneration;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.codegeneration.cache.ElementCache;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.PrivateComponents;
import io.intino.tara.magritte.Layer;

import java.io.File;
import java.util.Map;

public abstract class Renderer {
	protected final CompilationContext compilationContext;
	protected final ElementHelper elementHelper;
	protected final Target target;

	protected Renderer(CompilationContext compilationContext, Target target) {
		this.compilationContext = compilationContext;
		this.elementHelper = new ElementHelper();
		this.target = target;
	}

	public void execute() {
		render();
	}

	protected abstract void render();

	public String project() {
		return compilationContext.project();
	}

	public String boxName() {
		return compilationContext.boxName();
	}

	protected CompilerConfiguration configuration() {
		return compilationContext.configuration();
	}

	protected String packageName() {
		return compilationContext.packageName();
	}

	protected String module() {
		return compilationContext.module();
	}

	protected String parent() {
		return compilationContext.parent();
	}

	protected File root() {
		return compilationContext.root(target);
	}

	protected File res() {
		return compilationContext.res(target);
	}

	protected File src() {
		return compilationContext.src(target);
	}

	protected File gen() {
		return compilationContext.gen(target);
	}

	protected Map<String, String> classes() {
		return compilationContext.classes();
	}

	protected ElementCache cache() {
		return compilationContext.cache();
	}

	public FrameBuilder buildBaseFrame() {
		return new FrameBuilder().add("box", boxName()).add("package", compilationContext.packageName());
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
		compilationContext.cache().add(element);
	}

}
