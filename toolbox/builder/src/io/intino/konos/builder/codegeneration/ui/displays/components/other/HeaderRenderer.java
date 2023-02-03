package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.builder.context.CompilationContext;

import static io.intino.konos.model.VisualizationComponents.Header;

public class HeaderRenderer extends SizedRenderer<Header> {

	public HeaderRenderer(CompilationContext compilationContext, Header component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		result.add("position", element.position().name().toLowerCase());
		result.add("elevation", element.elevation());
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("header", "");
	}
}
