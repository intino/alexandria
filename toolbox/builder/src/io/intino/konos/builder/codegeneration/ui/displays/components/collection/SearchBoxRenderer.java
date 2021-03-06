package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.CatalogComponents.SearchBox;

public class SearchBoxRenderer extends BindingCollectionRenderer<SearchBox> {

	public SearchBoxRenderer(CompilationContext compilationContext, SearchBox component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder, element.collections());
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		String placeholder = element.placeholder();
		if (placeholder == null || placeholder.isEmpty()) return result;
		result.add("placeholder", placeholder);
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("searchbox", "");
	}

}
