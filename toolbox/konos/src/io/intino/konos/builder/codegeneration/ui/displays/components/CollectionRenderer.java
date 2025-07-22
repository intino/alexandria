package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.CatalogComponents;
import io.intino.konos.dsl.CatalogComponents.Collection;
import io.intino.konos.dsl.Navigable;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

public class CollectionRenderer<T extends Collection> extends SizedRenderer<T> {

	public CollectionRenderer(CompilationContext compilationContext, T component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		Integer width = itemWidth();
		result.add("collection");
		if (element.i$(conceptOf(Navigable.class)))
			result.add("navigable", element.a$(Navigable.class).position().name());
		if (element.sourceClass() != null) result.add("sourceClass", element.sourceClass());
		if (element.i$(conceptOf(CatalogComponents.Magazine.class)) ||element.i$(conceptOf(CatalogComponents.List.class)) || element.i$(conceptOf(CatalogComponents.Table.class)) || element.i$(conceptOf(CatalogComponents.Grid.class)))
			result.add("pageSize", element.pageSize());
		result.add("itemHeight", itemHeight());
		if (width != null) result.add("itemWidth", width);
		result.add("scrollingMark", element.scrollingMark());
		if (element.isSelectable()) result.add("selection", element.asSelectable().multiple() ? "multiple" : "single");
		if (element.noItemsMessage() != null) result.add("noItemsMessage", element.noItemsMessage());
		if (element.noItemsFoundMessage() != null) result.add("noItemsFoundMessage", element.noItemsFoundMessage());
		addColumns(result);
		return result;
	}

	@Override
	protected boolean addSpecificTypes(FrameBuilder builder) {
		super.addSpecificTypes(builder);

		builder.add(Collection.class.getSimpleName(), typeOf(element));
		if (element.sourceClass() != null) builder.add("sourceClass", element.sourceClass());
		if (element.i$(conceptOf(CatalogComponents.Moldable.class)))
			builder.add("componentType", firstUpperCase(nameOf(element.a$(CatalogComponents.Moldable.class).mold(0).item())));
		builder.add("itemClass", element.itemClass() != null ? element.itemClass() : "java.lang.Void");

		addMethodsFrame(builder);

		return false;
	}

	private void addMethodsFrame(FrameBuilder builder) {
		FrameBuilder result = addOwner(buildBaseFrame()).add("method").add(Collection.class.getSimpleName()).add(className(element.getClass()));
		result.add("name", nameOf(element));
		if (!isExposed(element)) result.add("concreteBox", boxName());
		if (element.sourceClass() != null) result.add("sourceClass", element.sourceClass());
		result.add("itemClass", element.itemClass() != null ? element.itemClass() : "java.lang.Void");
		result.add("itemVariable", "item");
		addSelectionMethod(result);
		if (element.i$(CatalogComponents.Moldable.class))
			element.a$(CatalogComponents.Moldable.class).moldList().forEach(m -> addItemFrame(m.item(), result));
		builder.add("methods", result);
	}

	private void addItemFrame(CatalogComponents.Moldable.Mold.Item item, FrameBuilder builder) {
		FrameBuilder result = buildBaseFrame().add("item");
		if (!isExposed(item)) result.add("concreteBox", boxName());
		result.add("methodVisibility", element.i$(conceptOf(CatalogComponents.Table.class)) || element.i$(conceptOf(CatalogComponents.DynamicTable.class)) ? "private" : "public");
		result.add("name", nameOf(item));
		result.add("methodName", element.i$(conceptOf(CatalogComponents.Table.class)) || element.i$(conceptOf(CatalogComponents.DynamicTable.class)) ? nameOf(item) : "");
		String itemClass = element.itemClass();
		result.add("itemClass", new FrameBuilder("itemClass", element.i$(conceptOf(CatalogComponents.Map.class)) ? "map" : "").add("value", itemClass != null ? itemClass : "java.lang.Void"));
		result.add("itemVariable", new FrameBuilder("itemVariable", element.i$(conceptOf(CatalogComponents.Map.class)) ? "map" : "").add("value", "item"));
		builder.add("item", result);
	}

	private void addSelectionMethod(FrameBuilder builder) {
		if (!element.isSelectable()) return;
		builder.add("selectionMethod", new FrameBuilder("selectionMethod"));
	}

	private int itemHeight() {
		if (!element.i$(conceptOf(CatalogComponents.Moldable.class))) return 100;
		return element.a$(CatalogComponents.Moldable.class).moldList().stream().mapToInt(m -> m.item().height()).max().orElse(100);
	}

	private Integer itemWidth() {
		if (!element.i$(conceptOf(CatalogComponents.Moldable.class))) return null;
		int width = element.a$(CatalogComponents.Moldable.class).moldList().stream().mapToInt(m -> m.item().width()).sum();
		return width > 0 ? width : null;
	}

	private void addColumns(FrameBuilder result) {
		if (!element.i$(conceptOf(CatalogComponents.Grid.class))) return;
		CatalogComponents.Grid grid = element.a$(CatalogComponents.Grid.class);
		grid.columnList().forEach(c -> result.add("column", frameOf(c)));
	}

	private FrameBuilder frameOf(CatalogComponents.Grid.Column column) {
		FrameBuilder result = new FrameBuilder("column");
		result.add("name", column.name$());
		result.add("label", column.label());
		if (column.width() != -1) result.add("width", column.width());
		if (column.sortable()) result.add("sortable", column.sortable());
		if (column.fixed()) result.add("fixed", column.fixed());
		if (column.pattern() != null) result.add("pattern", column.pattern());
		result.add("itemClass", element.itemClass() != null ? element.itemClass() : "java.lang.Void");
		result.add("type", column.isClickable() ? "Link" : column.type().name());
		if (!column.isAddressable()) return result;
		CatalogComponents.Grid.Column.Addressable addressable = column.asAddressable();
		result.add("address", addressable.addressableResource() != null ? addressable.addressableResource().path() : "");
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("collection", "");
	}
}
