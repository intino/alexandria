package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.VisualizationComponents.Stepper;

public class StepperRenderer extends ComponentRenderer<Stepper> {

	public StepperRenderer(CompilationContext compilationContext, Stepper component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		properties.add("orientation", element.orientation().name());
		properties.add("position", element.position().name());
		properties.add("style", element.style().name());
		return properties;
	}
}
