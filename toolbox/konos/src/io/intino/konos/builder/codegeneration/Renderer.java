package io.intino.konos.builder.codegeneration;

import io.intino.builder.CompilerConfiguration;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.dsl.CatalogComponents;
import io.intino.konos.dsl.HelperComponents;
import io.intino.konos.dsl.Service;
import io.intino.magritte.framework.Layer;

import java.io.File;
import java.util.Map;

import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

public abstract class Renderer {
	protected final CompilationContext context;
	protected final ElementHelper elementHelper;

	protected Renderer(CompilationContext context) {
		this.context = context;
		this.elementHelper = new ElementHelper();
	}

	public void execute() throws KonosException {
		render();
	}

	protected abstract void render() throws KonosException;

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

	protected File root(Target target) {
		return context.root(target);
	}

	protected File res(Target target) {
		return context.res(target);
	}

	protected File src(Target target) {
		return context.src(target);
	}

	protected File gen(Target target) {
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
		if (element.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class))) return false;
		if (element.i$(conceptOf(HelperComponents.Row.class))) return false;
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

	protected boolean isMobile(Service.UI service) {
		return service.targets().contains(Service.UI.Targets.Android) || service.targets().contains(Service.UI.Targets.IOS);
	}
}
