package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.VisualizationComponents;

public class KpiRenderer extends ComponentRenderer<VisualizationComponents.Kpi> {

	public KpiRenderer(CompilationContext compilationContext, VisualizationComponents.Kpi component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		properties.add("mode", element.mode().name());
		properties.add("label", element.label());
		properties.add("unit", element.unit());
		properties.add("backgroundColor", element.backgroundColor());
		properties.add("textColor", element.textColor());
		properties.add("value", element.value());
		return properties;
	}

}
