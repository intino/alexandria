package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.VisualizationComponents.Stepper;

public class StepRenderer extends ComponentRenderer<Stepper.Step> {

	public StepRenderer(CompilationContext compilationContext, Stepper.Step component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
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
