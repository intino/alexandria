package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.CatalogComponents.Sorting;

public class SortingRenderer extends BindingCollectionRenderer<Sorting> {

	public SortingRenderer(CompilationContext compilationContext, Sorting component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder, element.collections());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("sorting", "");
	}
}
