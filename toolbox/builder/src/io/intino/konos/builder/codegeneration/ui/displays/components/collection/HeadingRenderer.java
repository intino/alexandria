package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.CatalogComponents;

public class HeadingRenderer extends CollectionComponentRenderer<CatalogComponents.Moldable.Mold.Heading> {

	public HeadingRenderer(CompilationContext compilationContext, CatalogComponents.Moldable.Mold.Heading component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("item", "");
	}
}
