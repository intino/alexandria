package io.intino.konos.builder.codegeneration;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.tara.magritte.Layer;

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
		return false;
	}

	protected boolean isRoot(Layer element) {
		return element.core$().owner() == null || element.core$().owner() == element.core$().model();
	}
}
