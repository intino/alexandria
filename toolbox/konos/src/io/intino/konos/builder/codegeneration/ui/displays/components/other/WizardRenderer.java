package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.VisualizationComponents.Wizard;

public class WizardRenderer extends ComponentRenderer<Wizard> {

	public WizardRenderer(CompilationContext compilationContext, Wizard component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		properties.add("orientation", element.orientation().name());
		properties.add("position", element.position().name());
		properties.add("style", element.style().name());
		if (element.confirmMessage() != null) properties.add("confirmMessage", element.confirmMessage());
		return properties;
	}
}
