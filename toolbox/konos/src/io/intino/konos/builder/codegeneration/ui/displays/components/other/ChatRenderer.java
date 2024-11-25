package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.VisualizationComponents;

public class ChatRenderer extends SizedRenderer<VisualizationComponents.Chat> {

	public ChatRenderer(CompilationContext compilationContext, VisualizationComponents.Chat component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		properties.add("label", element.label());
		properties.add("view", element.view().name());
		properties.add("messageFlow", element.messageFlow().name());
		if (element.emptyMessage() != null) properties.add("emptyMessage", element.emptyMessage());
		if (element.loadingImage() != null) properties.add("loadingImage", resourceMethodFrame("loadingImage", element.loadingImage()));
		if (element.incomingImage() != null) properties.add("incomingImage", resourceMethodFrame("incomingImage", element.incomingImage()));
		if (element.outgoingImage() != null) properties.add("outgoingImage", resourceMethodFrame("outgoingImage", element.outgoingImage()));
		return properties;
	}

}
