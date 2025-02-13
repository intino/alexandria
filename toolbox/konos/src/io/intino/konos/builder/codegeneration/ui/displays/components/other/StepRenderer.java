package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.VisualizationComponents;

public class StepRenderer extends ComponentRenderer<VisualizationComponents.Wizard.Step> {

	public StepRenderer(CompilationContext compilationContext, VisualizationComponents.Wizard.Step component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		if (element.isMaterialIcon()) {
			properties.add("materialicon");
			properties.add("icon", element.asMaterialIcon().icon());
		}
		return properties;
	}
}
