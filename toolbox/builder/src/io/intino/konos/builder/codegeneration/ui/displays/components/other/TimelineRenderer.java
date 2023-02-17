package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;

import static io.intino.konos.model.OtherComponents.Timeline;

public class TimelineRenderer extends ComponentRenderer<Timeline> {

	public TimelineRenderer(CompilationContext compilationContext, Timeline component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

}
