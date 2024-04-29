package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.CatalogComponents;

public class ItemRenderer extends CollectionComponentRenderer<CatalogComponents.Moldable.Mold.Item> {

	public ItemRenderer(CompilationContext compilationContext, CatalogComponents.Moldable.Mold.Item component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("item", "");
	}
}
