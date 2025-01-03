package io.intino.konos.builder.codegeneration.ui;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.CodeGenerationHelper;
import io.intino.konos.dsl.*;
import io.intino.magritte.framework.Layer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;
import static java.util.stream.Collectors.toList;

public abstract class UIRenderer extends Renderer {

	protected UIRenderer(CompilationContext compilationContext) {
		super(compilationContext);
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

	protected String path(Display display, Target target) {
		return CodeGenerationHelper.displayPath(null, typeOf(display), target);
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

	protected List<Component> components(PassiveView passiveView) {
		List<Component> components = new ArrayList<>();
		if (passiveView.i$(conceptOf(Block.class))) components.addAll(passiveView.a$(Block.class).componentList());
		if (passiveView.i$(conceptOf(io.intino.konos.dsl.Template.class)))
			components.addAll(passiveView.a$(io.intino.konos.dsl.Template.class).componentList());
		if (passiveView.i$(conceptOf(OtherComponents.Snackbar.class)))
			components.addAll(passiveView.a$(OtherComponents.Snackbar.class).componentList());
		if (passiveView.i$(conceptOf(VisualizationComponents.Stepper.class)))
			components.addAll(passiveView.a$(VisualizationComponents.Stepper.class).stepList());
		if (passiveView.i$(conceptOf(VisualizationComponents.Stepper.Step.class)))
			components.addAll(passiveView.a$(VisualizationComponents.Stepper.Step.class).componentList());
		if (passiveView.i$(conceptOf(VisualizationComponents.Header.class)))
			components.addAll(passiveView.a$(VisualizationComponents.Header.class).componentList());
		if (passiveView.i$(conceptOf(OtherComponents.Selector.class))) {
			if (passiveView.i$(conceptOf(OtherComponents.Selector.CollectionBox.class)) && passiveView.a$(OtherComponents.Selector.CollectionBox.class).source() != null)
				components.add(passiveView.a$(OtherComponents.Selector.CollectionBox.class).source());
			else components.addAll(passiveView.a$(OtherComponents.Selector.class).componentList());
		}
		if (passiveView.i$(conceptOf(OtherComponents.User.class)))
			components.addAll(passiveView.a$(OtherComponents.User.class).componentList());
		if (passiveView.i$(conceptOf(CatalogComponents.Table.class)))
			components.addAll(passiveView.a$(CatalogComponents.Table.class).moldList().stream().filter(m -> m.heading() != null).map(CatalogComponents.Moldable.Mold::heading).collect(toList()));
		if (passiveView.i$(conceptOf(CatalogComponents.DynamicTable.class)))
			components.addAll(passiveView.a$(CatalogComponents.DynamicTable.class).moldList().stream().filter(m -> m.heading() != null).map(CatalogComponents.Moldable.Mold::heading).collect(toList()));
		if (passiveView.i$(conceptOf(CatalogComponents.Moldable.Mold.Heading.class)))
			components.addAll(passiveView.a$(CatalogComponents.Moldable.Mold.Heading.class).componentList());
		if (passiveView.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class)))
			components.addAll(passiveView.a$(CatalogComponents.Moldable.Mold.Item.class).componentList());
		if (passiveView.i$(conceptOf(InteractionComponents.Toolbar.class)))
			components.addAll(passiveView.a$(InteractionComponents.Toolbar.class).componentList());
		if (passiveView.i$(conceptOf(OtherComponents.Dialog.class)))
			components.addAll(passiveView.a$(OtherComponents.Dialog.class).componentList());
		if (passiveView.i$(conceptOf(OtherComponents.DecisionDialog.class)))
			components.add(passiveView.a$(OtherComponents.DecisionDialog.class).selector());
		if (passiveView.i$(conceptOf(OtherComponents.CollectionDialog.class)))
			components.add(passiveView.a$(OtherComponents.CollectionDialog.class).collection());
		return components;
	}

	protected boolean isAlexandriaProject() {
		return context.project().equals("alexandria");
	}

	protected boolean hasConcreteNotifier(PassiveView element) {
		if (element.i$(conceptOf(Display.Accessible.class))) return true;
		return !element.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class)) &&
				!element.i$(conceptOf(CatalogComponents.Table.class)) &&
				!element.i$(conceptOf(CatalogComponents.DynamicTable.class)) &&
				!element.i$(conceptOf(HelperComponents.Row.class)) &&
				!element.i$(conceptOf(io.intino.konos.dsl.Template.class));
	}

	protected boolean hasConcreteRequester(PassiveView element) {
		if (element.i$(conceptOf(Display.Accessible.class))) return true;
		return !element.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class)) &&
				!element.i$(conceptOf(CatalogComponents.Table.class)) &&
				!element.i$(conceptOf(CatalogComponents.DynamicTable.class)) &&
				!element.i$(conceptOf(HelperComponents.Row.class)) &&
				!element.i$(conceptOf(io.intino.konos.dsl.Template.class));
	}

	protected String notifierName(Layer element) {
		return passiveViewName(element);
	}

	protected String requesterName(Layer element) {
		return passiveViewName(element);
	}

	private String passiveViewName(Layer element) {
		if (element.i$(conceptOf(io.intino.konos.dsl.Template.class))) return "Template";
		if (element.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class))) return "Item";
		if (element.i$(conceptOf(HelperComponents.Row.class))) return "Row";
		if (element.i$(conceptOf(CatalogComponents.Table.class))) return "Table";
		if (element.i$(conceptOf(CatalogComponents.DynamicTable.class))) return "DynamicTable";
		return firstUpperCase(element.name$());
	}

	protected String requesterTypeOf(Display display) {
		if (display.i$(conceptOf(io.intino.konos.dsl.Template.class)))
			return "io.intino.alexandria.ui.displays.requesters.Template";
		if (display.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class)))
			return "io.intino.alexandria.ui.displays.requesters.Item";
		if (display.i$(conceptOf(HelperComponents.Row.class))) return "io.intino.alexandria.ui.displays.requesters.Row";
		if (display.i$(conceptOf(CatalogComponents.Table.class)))
			return "io.intino.alexandria.ui.displays.requesters.Table";
		if (display.i$(conceptOf(CatalogComponents.DynamicTable.class)))
			return "io.intino.alexandria.ui.displays.requesters.DynamicTable";
		return firstUpperCase(nameOf(display));
	}

	protected FrameBuilder notifierImportFrame(Display element, boolean accessible) {
		FrameBuilder result = buildBaseFrame().add("notifierImport");
		result.add("name", element.name$());
		if (element.i$(conceptOf(io.intino.konos.dsl.Template.class))) result.add("template");
		if (element.i$(conceptOf(HelperComponents.Row.class))) result.add("row");
		if (element.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class))) result.add("item");
		if (element.i$(conceptOf(CatalogComponents.Table.class))) result.add("table");
		if (element.i$(conceptOf(CatalogComponents.DynamicTable.class))) result.add("dynamictable");
		if (!accessible && element.isAccessible()) result.add("accessible");
		return result;
	}

}