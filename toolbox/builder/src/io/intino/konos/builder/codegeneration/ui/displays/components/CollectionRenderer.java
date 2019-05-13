package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.AbstractNavigable;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.CatalogComponents.Collection;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;

public class CollectionRenderer extends SizedRenderer<Collection> {

	public CollectionRenderer(Settings settings, Collection component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder frameBuilder() {
		FrameBuilder frame = super.frameBuilder();
		addHeadings(frame);
		return frame;
	}

	private void addHeadings(FrameBuilder builder) {
		element.moldList().forEach(m -> addHeading(m, builder));
	}

	private void addHeading(Collection.Mold mold, FrameBuilder builder) {
		if (mold.heading() == null) return;
		builder.add("heading", childFrame(mold.heading()));
		builder.add("component", childFrame(mold.heading()));
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		result.add("collection");
		if (element.i$(AbstractNavigable.class)) result.add("navigable", element.a$(AbstractNavigable.class).position().name());
		if (element.sourceClass() != null) result.add("sourceClass", element.sourceClass());
		result.add("pageSize", element.pageSize());
		result.add("itemHeight", itemHeight());
		result.add("scrollingMark", element.scrollingMark());
		if (element.isSelectable()) result.add("selection", element.asSelectable().multiple() ? "multiple" : "single");
		if (element.noItemsMessage() != null) result.add("noItemsMessage", element.noItemsMessage());
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
		FrameBuilder result = addOwner(baseFrameBuilder()).add("method").add(Collection.class.getSimpleName()).add(className(element.getClass()));
		result.add("name", nameOf(element));
		if (element.sourceClass() != null) result.add("sourceClass", element.sourceClass());
		if (element.itemClass() != null) {
			result.add("itemClass", element.itemClass());
			result.add("itemVariable", "item");
		}
		addSelectionMethod(result);
		element.moldList().forEach(m -> addItemFrame(m.item(), result));
		builder.add("methods", result);
	}

	private void addItemFrame(Collection.Mold.Item item, FrameBuilder builder) {
		FrameBuilder result = baseFrameBuilder().add("item");
		result.add("methodAccessibility", element.i$(CatalogComponents.Table.class) ? "private" : "public");
		result.add("name", nameOf(item));
		result.add("methodName", element.i$(CatalogComponents.Table.class) ? nameOf(item) : "");
		if (!element.i$(CatalogComponents.Table.class)) {
			result.add("addPromise", "addPromise");
			result.add("insertPromise", "insertPromise");
		}
		String itemClass = element.itemClass();
		if (itemClass != null) {
			result.add("itemClass", itemClass);
			result.add("itemVariable", "item");
		}
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
