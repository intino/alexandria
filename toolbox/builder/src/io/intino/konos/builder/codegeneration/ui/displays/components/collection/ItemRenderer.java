package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.CatalogComponents;

public class ItemRenderer extends CollectionComponentRenderer<CatalogComponents.Moldable.Mold.Item> {

	public ItemRenderer(CompilationContext compilationContext, CatalogComponents.Moldable.Mold.Item component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("item", "");
	}
}
