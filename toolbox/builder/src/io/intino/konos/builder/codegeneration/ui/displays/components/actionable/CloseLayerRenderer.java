package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.InteractionComponents;

public class CloseLayerRenderer extends ActionableRenderer {

	public CloseLayerRenderer(CompilationContext context, InteractionComponents.Actionable component, RendererWriter provider) {
		super(context, component, provider);
	}

}
