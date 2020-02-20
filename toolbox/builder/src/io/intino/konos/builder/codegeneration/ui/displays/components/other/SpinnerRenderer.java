package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.VisualizationComponents.Spinner;

public class SpinnerRenderer extends ComponentRenderer<Spinner> {

	public SpinnerRenderer(CompilationContext compilationContext, Spinner component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		properties.add("mode", element.mode().name());
		return properties;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("spinner", "");
	}
}
