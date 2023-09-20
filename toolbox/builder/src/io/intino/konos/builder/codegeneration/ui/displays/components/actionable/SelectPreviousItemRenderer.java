package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.ActionableComponents;
import io.intino.konos.model.CatalogComponents;

public class SelectPreviousItemRenderer extends ActionableRenderer {

	public SelectPreviousItemRenderer(CompilationContext context, ActionableComponents.Actionable component, RendererWriter provider) {
		super(context, component, provider);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder);
	}

	protected void addBinding(FrameBuilder builder) {
		CatalogComponents.Collection collection = element.asSelectPreviousItem().collection();
		if (collection == null) return;
		FrameBuilder result = new FrameBuilder("binding", "openpreviousitem").add("name", nameOf(element));
		result.add("collection", nameOf(collection));
		builder.add("binding", result);
	}

}
