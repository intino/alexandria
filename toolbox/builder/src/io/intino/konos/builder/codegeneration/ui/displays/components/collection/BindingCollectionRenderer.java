package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.Component;

import java.util.List;

public class BindingCollectionRenderer<C extends Component> extends ComponentRenderer<C> {

	public BindingCollectionRenderer(CompilationContext compilationContext, C component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	protected void addBinding(FrameBuilder builder, List<CatalogComponents.Collection> collections) {
		if (collections.size() <= 0) return;
		FrameBuilder result = new FrameBuilder("binding", type()).add("name", nameOf(element));
		collections.forEach(c -> result.add("collection", nameOf(c)));
		builder.add("binding", result);
	}

}
