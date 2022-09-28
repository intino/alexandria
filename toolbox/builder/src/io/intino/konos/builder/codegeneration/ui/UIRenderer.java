package io.intino.konos.builder.codegeneration.ui;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.CodeGenerationHelper;
import io.intino.konos.model.*;
import io.intino.magritte.framework.Layer;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

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

	protected String path(io.intino.konos.model.Display display) {
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
			   type.equalsIgnoreCase("dynamictable") ||
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
		return !element.i$(conceptOf(io.intino.konos.model.Template.class)) &&
				!element.i$(conceptOf(CatalogComponents.Table.class)) &&
				!element.i$(conceptOf(CatalogComponents.DynamicTable.class)) &&
				!element.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class)) &&
				!element.i$(conceptOf(HelperComponents.Row.class));
	}

	protected boolean hasConcreteNotifier(PassiveView element) {
		if (element.i$(conceptOf(Display.Accessible.class))) return true;
		return !element.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class)) &&
				!element.i$(conceptOf(CatalogComponents.Table.class)) &&
				!element.i$(conceptOf(CatalogComponents.DynamicTable.class)) &&
				!element.i$(conceptOf(HelperComponents.Row.class)) &&
				!element.i$(conceptOf(io.intino.konos.model.Template.class));
	}

	protected boolean hasConcreteRequester(PassiveView element) {
		if (element.i$(conceptOf(Display.Accessible.class))) return true;
		return !element.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class)) &&
				!element.i$(conceptOf(CatalogComponents.Table.class)) &&
				!element.i$(conceptOf(CatalogComponents.DynamicTable.class)) &&
				!element.i$(conceptOf(HelperComponents.Row.class)) &&
				!element.i$(conceptOf(io.intino.konos.model.Template.class));
	}

	protected String notifierName(Layer element) {
		return passiveViewName(element);
	}

	protected String requesterName(Layer element) {
		return passiveViewName(element);
	}

	private String passiveViewName(Layer element) {
		if (element.i$(conceptOf(io.intino.konos.model.Template.class))) return "Template";
		if (element.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class))) return "Item";
		if (element.i$(conceptOf(HelperComponents.Row.class))) return "Row";
		if (element.i$(conceptOf(CatalogComponents.Table.class))) return "Table";
		if (element.i$(conceptOf(CatalogComponents.DynamicTable.class))) return "DynamicTable";
		return firstUpperCase(element.name$());
	}

	protected String requesterTypeOf(Display display) {
		if (display.i$(conceptOf(io.intino.konos.model.Template.class))) return "io.intino.alexandria.ui.displays.requesters.Template";
		if (display.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class))) return "io.intino.alexandria.ui.displays.requesters.Item";
		if (display.i$(conceptOf(HelperComponents.Row.class))) return "io.intino.alexandria.ui.displays.requesters.Row";
		if (display.i$(conceptOf(CatalogComponents.Table.class))) return "io.intino.alexandria.ui.displays.requesters.Table";
		if (display.i$(conceptOf(CatalogComponents.DynamicTable.class))) return "io.intino.alexandria.ui.displays.requesters.DynamicTable";
		return firstUpperCase(nameOf(display));
	}

	protected FrameBuilder notifierImportFrame(Display element, boolean accessible) {
		FrameBuilder result = buildBaseFrame().add("notifierImport");
		result.add("name", element.name$());
		if (element.i$(conceptOf(io.intino.konos.model.Template.class))) result.add("template");
		if (element.i$(conceptOf(HelperComponents.Row.class))) result.add("row");
		if (element.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class))) result.add("item");
		if (element.i$(conceptOf(CatalogComponents.Table.class))) result.add("table");
		if (element.i$(conceptOf(CatalogComponents.DynamicTable.class))) result.add("dynamictable");
		if (!accessible && element.isAccessible()) result.add("accessible");
		return result;
	}

}