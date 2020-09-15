package io.intino.konos.builder.codegeneration;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.HelperComponents;
import io.intino.konos.model.graph.OtherComponents;
import io.intino.magritte.framework.Layer;

import java.io.File;
import java.util.Map;

public abstract class Renderer {
	protected final CompilationContext context;
	protected final ElementHelper elementHelper;
	protected final Target target;

	protected Renderer(CompilationContext context, Target target) {
		this.context = context;
		this.elementHelper = new ElementHelper();
		this.target = target;
	}

	public void execute() {
		render();
	}

	protected abstract void render();

	public String project() {
		return context.project();
	}

	public String boxName() {
		return context.boxName();
	}

	protected CompilerConfiguration configuration() {
		return context.configuration();
	}

	protected String packageName() {
		return context.packageName();
	}

	protected String module() {
		return context.module();
	}

	protected String parent() {
		return context.parent();
	}

	protected File root() {
		return context.root(target);
	}

	protected File res() {
		return context.res(target);
	}

	protected File src() {
		return context.src(target);
	}

	protected File gen() {
		return context.gen(target);
	}

	protected Map<String, String> classes() {
		return context.classes();
	}

	public FrameBuilder buildBaseFrame() {
		return new FrameBuilder().add("box", boxName()).add("package", context.packageName());
	}

	protected boolean isRendered(Layer element) {
		if (element == null) return false;
		if (element.i$(CatalogComponents.Collection.Mold.Item.class)) return false;
		if (element.i$(HelperComponents.Row.class)) return false;
		return !context.cache().isModified(element);
	}

	protected boolean isRoot(Layer element) {
		return element.core$().owner() == null || element.core$().owner() == element.core$().model();
	}

	protected boolean isCustomParameter(String value) {
		return value != null && value.startsWith("{");
	}

	protected String customParameterValue(String value) {
		return value != null ? value.substring(1, value.length() - 1) : "";
	}

}
