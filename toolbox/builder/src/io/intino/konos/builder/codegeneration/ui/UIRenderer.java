package io.intino.konos.builder.codegeneration.ui;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.CodeGenerationHelper;
import io.intino.konos.model.graph.*;
import io.intino.magritte.framework.Layer;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.konos.builder.helpers.Commons.firstUpperCase;

public abstract class UIRenderer extends Renderer {

	protected UIRenderer(CompilationContext compilationContext, Target target) {
		super(compilationContext, target);
	}

	public FrameBuilder buildFrame() {
		return buildBaseFrame();
	}

	protected String parent() {
		return context.parent();
	}

	protected Map<String, String> classes() {
		return context.classes();
	}

	protected Template setup(Template template) {
		return addFormats(template);
	}

	protected String path(io.intino.konos.model.graph.Display display) {
		return CodeGenerationHelper.displayPath(typeOf(display), target);
	}

	protected Template addFormats(Template template) {
		return Formatters.customize(template);
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
			   type.equalsIgnoreCase("table") ||
			   type.equalsIgnoreCase("list") ||
			   type.equalsIgnoreCase("map") ||
			   type.equalsIgnoreCase("magazine") ||
			   type.equalsIgnoreCase("block") ||
			   type.equalsIgnoreCase("item") ||
			   type.equalsIgnoreCase("row");
	}

	protected String patternOf(Service.UI.Resource resource) {
		if (resource.path().isEmpty()) return "\\\\/";
		Stream<String> split = Stream.of(resource.path().split("/"));
		return split.map(s -> s.startsWith(":") ? "([^\\\\/]*)" : s).collect(Collectors.joining("\\\\/"));
	}

	protected boolean hasAbstractClass(Layer element) {
		if (target == Target.Owner) return true;
		return !element.i$(io.intino.konos.model.graph.Template.class) &&
				!element.i$(CatalogComponents.Table.class) &&
				!element.i$(CatalogComponents.Collection.Mold.Item.class) &&
				!element.i$(HelperComponents.Row.class);
	}

	protected boolean hasConcreteNotifier(PassiveView element) {
		return !element.i$(CatalogComponents.Collection.Mold.Item.class) &&
				!element.i$(CatalogComponents.Table.class) &&
				!element.i$(HelperComponents.Row.class) &&
				!element.i$(io.intino.konos.model.graph.Template.class);
	}

	protected boolean hasConcreteRequester(PassiveView element) {
		return !element.i$(CatalogComponents.Collection.Mold.Item.class) &&
				!element.i$(CatalogComponents.Table.class) &&
				!element.i$(HelperComponents.Row.class) &&
				!element.i$(io.intino.konos.model.graph.Template.class);
	}

	protected String notifierName(Layer element) {
		return passiveViewName(element);
	}

	protected String requesterName(Layer element) {
		return passiveViewName(element);
	}

	private String passiveViewName(Layer element) {
		if (element.i$(io.intino.konos.model.graph.Template.class)) return "Template";
		if (element.i$(CatalogComponents.Collection.Mold.Item.class)) return "Item";
		if (element.i$(HelperComponents.Row.class)) return "Row";
		if (element.i$(CatalogComponents.Table.class)) return "Table";
		return firstUpperCase(element.name$());
	}

	protected String requesterTypeOf(Display display) {
		if (display.i$(io.intino.konos.model.graph.Template.class)) return "io.intino.alexandria.ui.displays.requesters.Template";
		if (display.i$(CatalogComponents.Collection.Mold.Item.class)) return "io.intino.alexandria.ui.displays.requesters.Item";
		if (display.i$(HelperComponents.Row.class)) return "io.intino.alexandria.ui.displays.requesters.Row";
		if (display.i$(CatalogComponents.Table.class)) return "io.intino.alexandria.ui.displays.requesters.Table";
		return firstUpperCase(nameOf(display));
	}

	protected FrameBuilder notifierImportFrame(Display element) {
		FrameBuilder result = buildBaseFrame().add("notifierImport");
		result.add("name", element.name$());
		if (element.i$(io.intino.konos.model.graph.Template.class)) result.add("template");
		if (element.i$(HelperComponents.Row.class)) result.add("row");
		if (element.i$(CatalogComponents.Collection.Mold.Item.class)) result.add("item");
		if (element.i$(CatalogComponents.Table.class)) result.add("table");
		return result;
	}

}