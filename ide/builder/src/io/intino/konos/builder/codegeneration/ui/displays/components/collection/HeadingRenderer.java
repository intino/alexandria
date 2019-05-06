package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.CatalogComponents;

public class HeadingRenderer extends CollectionComponentRenderer<CatalogComponents.Collection.Mold.Heading> {

	public HeadingRenderer(Settings settings, CatalogComponents.Collection.Mold.Heading component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("item", "");
	}
}
