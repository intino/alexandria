package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.CatalogComponents.Sorting;

public class SortingRenderer extends BindingCollectionRenderer<Sorting> {

	public SortingRenderer(CompilationContext compilationContext, Sorting component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		addOrderByProperties(properties);
		return properties;
	}

	private void addOrderByProperties(FrameBuilder properties) {
		if (!element.isOrderBy()) return;
		properties.add("mode", element.asOrderBy().mode() == Sorting.OrderBy.Mode.Ascending ? "asc" : "desc");
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
