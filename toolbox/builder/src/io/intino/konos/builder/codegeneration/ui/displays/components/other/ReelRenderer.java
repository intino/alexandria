package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.VisualizationComponents;
import io.intino.konos.model.VisualizationComponents.Reel;
import io.intino.konos.model.VisualizationComponents.Timeline;

public class ReelRenderer extends ComponentRenderer<Reel> {

	public ReelRenderer(CompilationContext compilationContext, Reel component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

}
