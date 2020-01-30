package io.intino.konos.builder.codegeneration.ui;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.CodeGenerationHelper;
import io.intino.konos.model.graph.PassiveView;
import io.intino.konos.model.graph.Service;
import io.intino.tara.magritte.Layer;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class UIRenderer extends Renderer {

	protected UIRenderer(CompilationContext compilationContext, Target target) {
		super(compilationContext, target);
	}

	public FrameBuilder buildFrame() {
		return buildBaseFrame();
	}

	protected String parent() {
		return compilationContext.parent();
	}

	protected Map<String, String> classes() {
		return compilationContext.classes();
	}

	protected Template setup(Template template) {
		return addFormats(template);
	}

	protected String path(io.intino.konos.model.graph.Display display) {
		return CodeGenerationHelper.displayPath(typeOf(display), target);
	}

	protected Template addFormats(Template template) {
		Formatters.customize(template);
		return template;
	}

	protected String typeOf(Layer element) {
		return elementHelper.typeOf(element);
	}

	protected String nameOf(Layer element) {
		return elementHelper.nameOf(element);
	}

	protected String shortId(Layer element) {
		return elementHelper.shortId(element);
	}

	protected String shortId(Layer element, String suffix) {
		return elementHelper.shortId(element, suffix);
	}

	protected <D extends PassiveView> boolean isBaseType(D element) {
		String type = typeOf(element);
		return type.equalsIgnoreCase("display") ||
			   type.equalsIgnoreCase("component") ||
			   type.equalsIgnoreCase("template") ||
			   type.equalsIgnoreCase("block") ||
			   type.equalsIgnoreCase("item") ||
			   type.equalsIgnoreCase("row");
	}

	protected String patternOf(Service.UI.Resource resource) {
		if (resource.path().isEmpty()) return "\\\\/";
		Stream<String> split = Stream.of(resource.path().split("/"));
		return split.map(s -> s.startsWith(":") ? "([^\\\\/]*)" : s).collect(Collectors.joining("\\\\/"));
	}

}