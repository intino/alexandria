package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.OtherComponents.HtmlViewer;

public class HtmlViewerRenderer extends ComponentRenderer<HtmlViewer> {

	public HtmlViewerRenderer(CompilationContext compilationContext, HtmlViewer component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		if (element.content() != null) properties.add("content", element.content());
		return properties;
	}

}
