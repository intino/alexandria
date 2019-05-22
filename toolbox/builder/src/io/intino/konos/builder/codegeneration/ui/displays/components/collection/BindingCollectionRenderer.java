package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.Component;

public class BindingCollectionRenderer<C extends Component> extends ComponentRenderer<C> {

	public BindingCollectionRenderer(Settings settings, C component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	void addBinding(FrameBuilder builder, io.intino.konos.model.graph.CatalogComponents.Collection collection) {
		if (collection == null) return;
		FrameBuilder result = new FrameBuilder("binding", type()).add("name", nameOf(element));
		result.add("collection", nameOf(collection));
		builder.add("binding", result);
	}

}
