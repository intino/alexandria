package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.VisualizationComponents.Spinner;

public class SpinnerRenderer extends ComponentRenderer<Spinner> {

	public SpinnerRenderer(CompilationContext compilationContext, Spinner component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		properties.add("mode", element.mode().name());
		if (element.size() != 0) properties.add("size", element.size());
		return properties;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("spinner", "");
	}
}
