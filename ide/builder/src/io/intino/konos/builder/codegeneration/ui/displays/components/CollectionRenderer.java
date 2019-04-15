package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.ChildComponents.Collection;
import io.intino.konos.model.graph.Component;
import org.siani.itrules.model.Frame;

import java.util.List;

public class CollectionRenderer extends SizedRenderer<Collection> {

	public CollectionRenderer(Settings settings, Collection component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame buildFrame() {
		Frame result = super.buildFrame();
		addMolds(result);
		return result;
	}

	private void addMolds(Frame frame) {
		element.moldList().forEach(m -> addMold(m, frame));
	}

	private void addMold(Collection.Mold mold, Frame frame) {
		Frame result = new Frame("mold");
		result.addSlot("heading", componentFrame(mold.heading().componentList()));
		result.addSlot("item", componentFrame(mold.item().componentList()));
		frame.addSlot("mold", result);
	}

	private Frame componentFrame(List<Component> componentList) {
		Frame result = new Frame("heading");
		componentList.stream().map(this::componentFrame).forEach(f -> result.addSlot("component", f));
		return result;
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		result.addTypes("collection");
		result.addSlot("source", element.sourceClass());
		result.addSlot("pageSize", element.pageSize());
		if (element.noItemsMessage() != null) result.addSlot("noItemsMessage", element.noItemsMessage());
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("collection", "");
	}
}
