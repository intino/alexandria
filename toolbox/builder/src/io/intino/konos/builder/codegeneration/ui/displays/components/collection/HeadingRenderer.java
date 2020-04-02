package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.CatalogComponents;

public class HeadingRenderer extends CollectionComponentRenderer<CatalogComponents.Collection.Mold.Heading> {

	public HeadingRenderer(CompilationContext compilationContext, CatalogComponents.Collection.Mold.Heading component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("item", "");
	}
}
