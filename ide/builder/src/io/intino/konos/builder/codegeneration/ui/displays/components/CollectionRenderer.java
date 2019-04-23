package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayRendererFactory;
import io.intino.konos.model.graph.ChildComponents;
import io.intino.konos.model.graph.ChildComponents.Collection;
import io.intino.konos.model.graph.PrivateComponents;
import org.siani.itrules.model.Frame;

import java.util.List;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static java.util.stream.Collectors.toList;

public class CollectionRenderer extends SizedRenderer<Collection> {

	public CollectionRenderer(Settings settings, Collection component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame();
		addHeadings(frame);
		createRowComponent();
		return frame;
	}

	private void createRowComponent() {
		if (!element.i$(ChildComponents.Table.class)) return;
		List<Collection.Mold.Item> itemList = element.moldList().stream().map(Collection.Mold::item).collect(toList());
		String name = firstUpperCase(element.name$()) + "Row";
		if (element.graph().privateComponentsList().size() <= 0) element.graph().create().privateComponents();
		PrivateComponents.Row row = element.graph().privateComponents(0).rowList().stream().filter(c -> c.name$().equals(name)).findFirst().orElse(null);
		if (row == null) row = element.graph().privateComponents(0).create(name).row(itemList);
		new DisplayRendererFactory().renderer(settings, row, templateProvider, target).execute();
	}

	private void addHeadings(Frame frame) {
		element.moldList().forEach(m -> addHeading(m, frame));
	}

	private void addHeading(Collection.Mold mold, Frame frame) {
		if (mold.heading() == null) return;
		frame.addSlot("heading", childFrame(mold.heading()));
		frame.addSlot("component", childFrame(mold.heading()));
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		result.addTypes("collection");
		if (element.sourceClass() != null) result.addSlot("sourceClass", element.sourceClass());
		result.addSlot("pageSize", element.pageSize());
		result.addSlot("itemHeight", itemHeight());
		addItemWidths(result);
		result.addSlot("scrollingMark", element.scrollingMark());
		if (element.noItemsMessage() != null) result.addSlot("noItemsMessage", element.noItemsMessage());
		return result;
	}

	@Override
	protected boolean addSpecificTypes(Frame frame) {
		super.addSpecificTypes(frame);

		frame.addTypes(Collection.class.getSimpleName(), typeOf(element));
		if (element.sourceClass() != null) frame.addSlot("sourceClass", element.sourceClass());
		frame.addSlot("componentType", firstUpperCase(nameOf(element.mold(0).item())));
		frame.addSlot("itemClass", element.itemClass() != null ? element.itemClass() : "java.lang.Void");

		addMethodsFrame(frame);

		return false;
	}

	private void addMethodsFrame(Frame frame) {
		Frame methodsFrame = addOwner(baseFrame()).addTypes("method", Collection.class.getSimpleName(), className(element.getClass()));
		methodsFrame.addSlot("name", nameOf(element));
		if (element.sourceClass() != null) methodsFrame.addSlot("sourceClass", element.sourceClass());
		if (element.itemClass() != null) {
			methodsFrame.addSlot("itemClass", element.itemClass());
			methodsFrame.addSlot("itemVariable", "item");
		}
		element.moldList().forEach(m -> addItemFrame(m.item(), methodsFrame));
		frame.addSlot("methods", methodsFrame);
	}

	private void addItemFrame(Collection.Mold.Item item, Frame frame) {
		Frame result = baseFrame().addTypes("item");
		result.addSlot("methodAccessibility", element.i$(ChildComponents.Table.class) ? "private" : "public");
		result.addSlot("name", nameOf(item));
		result.addSlot("methodName", element.i$(ChildComponents.Table.class) ? nameOf(item) : "");
		if (!element.i$(ChildComponents.Table.class)) {
			result.addSlot("addPromise", "addPromise");
			result.addSlot("insertPromise", "insertPromise");
		}
		String itemClass = element.itemClass();
		if (itemClass != null) {
			result.addSlot("itemClass", itemClass);
			result.addSlot("itemVariable", "item");
		}
		frame.addSlot("item", result);
	}

	private int itemHeight() {
		return element.moldList().stream().mapToInt(m -> m.item().height()).max().orElse(100);
	}

	private void addItemWidths(Frame frame) {
		int countMolds = element.moldList().size();
		int defaultWidth = countMolds > 0 ? Math.round(100 / countMolds) : 100;
		element.moldList().stream().mapToInt(m -> m.item().width() != -1 ? m.item().width() : defaultWidth).boxed().forEach(w -> frame.addSlot("itemWidth", w));
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("collection", "");
	}
}
