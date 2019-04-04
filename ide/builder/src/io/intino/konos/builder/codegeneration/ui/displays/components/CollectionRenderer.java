package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.ChildComponents.Collection;
import org.siani.itrules.model.Frame;

public class CollectionRenderer extends SizedRenderer<Collection> {

	public CollectionRenderer(Settings settings, Collection component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		result.addTypes("collection");
		result.addSlot("source", element.source().name$());
		result.addSlot("pageSize", element.pageSize());
		if (element.noItemsMessage() != null) result.addSlot("noItemsMessage", element.noItemsMessage());
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("collection", "");
	}
}
