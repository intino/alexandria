package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.VisualizationComponents.Eventline;

public class EventlineRenderer extends ComponentRenderer<Eventline> {

	public EventlineRenderer(CompilationContext compilationContext, Eventline component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		if (element.arrangement() != null) properties.add("arrangement", element.arrangement().name());
		if (element.toolbarArrangement() != null)
			properties.add("toolbarArrangement", element.toolbarArrangement().name());
		return properties;
	}

}
