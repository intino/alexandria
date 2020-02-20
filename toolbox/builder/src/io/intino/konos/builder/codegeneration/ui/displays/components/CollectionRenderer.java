package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.CatalogComponents.Collection;
import io.intino.konos.model.graph.Navigable;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;

public class CollectionRenderer<T extends Collection> extends SizedRenderer<T> {

	public CollectionRenderer(CompilationContext compilationContext, T component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		result.add("collection");
		if (element.i$(Navigable.class)) result.add("navigable", element.a$(Navigable.class).position().name());
		if (element.sourceClass() != null) result.add("sourceClass", element.sourceClass());
		if (element.i$(CatalogComponents.List.class) || element.i$(CatalogComponents.Table.class)) result.add("pageSize", element.pageSize());
		result.add("itemHeight", itemHeight());
		result.add("scrollingMark", element.scrollingMark());
		if (element.isSelectable()) result.add("selection", element.asSelectable().multiple() ? "multiple" : "single");
		if (element.noItemsMessage() != null) result.add("noItemsMessage", element.noItemsMessage());
		if (element.noItemsFoundMessage() != null) result.add("noItemsFoundMessage", element.noItemsFoundMessage());
		return result;
	}

	@Override
	protected boolean addSpecificTypes(FrameBuilder builder) {
		super.addSpecificTypes(builder);

		builder.add(Collection.class.getSimpleName(), typeOf(element));
		if (element.sourceClass() != null) builder.add("sourceClass", element.sourceClass());
		builder.add("componentType", firstUpperCase(nameOf(element.mold(0).item())));
		builder.add("itemClass", element.itemClass() != null ? element.itemClass() : "java.lang.Void");

		addMethodsFrame(builder);

		return false;
	}

	private void addMethodsFrame(FrameBuilder builder) {
		FrameBuilder result = addOwner(buildBaseFrame()).add("method").add(Collection.class.getSimpleName()).add(className(element.getClass()));
		result.add("name", nameOf(element));
		if (!belongsToAccessible(element)) result.add("concreteBox", boxName());
		if (element.sourceClass() != null) result.add("sourceClass", element.sourceClass());
		result.add("itemClass", element.itemClass() != null ? element.itemClass() : "java.lang.Void");
		result.add("itemVariable", "item");
		addSelectionMethod(result);
		element.moldList().forEach(m -> addItemFrame(m.item(), result));
		builder.add("methods", result);
	}

	private void addItemFrame(Collection.Mold.Item item, FrameBuilder builder) {
		FrameBuilder result = buildBaseFrame().add("item");
		if (!belongsToAccessible(item)) result.add("concreteBox", boxName());
		result.add("methodAccessibility", element.i$(CatalogComponents.Table.class) ? "private" : "public");
		result.add("name", nameOf(item));
		result.add("methodName", element.i$(CatalogComponents.Table.class) ? nameOf(item) : "");
		String itemClass = element.itemClass();
		result.add("itemClass", new FrameBuilder("itemClass", element.i$(CatalogComponents.Map.class) ? "map" : "").add("value", itemClass != null ? itemClass : "java.lang.Void"));
		result.add("itemVariable", new FrameBuilder("itemVariable", element.i$(CatalogComponents.Map.class) ? "map" : "").add("value", "item"));
		builder.add("item", result);
	}

	private void addSelectionMethod(FrameBuilder builder) {
		if (!element.isSelectable()) return;
		builder.add("selectionMethod", new FrameBuilder("selectionMethod"));
	}

	private int itemHeight() {
		return element.moldList().stream().mapToInt(m -> m.item().height()).max().orElse(100);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("collection", "");
	}
}
