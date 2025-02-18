package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.VisualizationComponents.WizardNavigator;

public class WizardNavigatorRenderer extends ComponentRenderer<WizardNavigator> {

	public WizardNavigatorRenderer(CompilationContext compilationContext, WizardNavigator component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		if (element.selected() != null) result.add("selected", element.selected());
		return result;
	}

	protected void addBinding(FrameBuilder builder) {
		FrameBuilder result = new FrameBuilder("binding", type()).add("name", nameOf(element));
		result.add("component", nameOf(element.wizard()));
		builder.add("binding", result);
	}

}
