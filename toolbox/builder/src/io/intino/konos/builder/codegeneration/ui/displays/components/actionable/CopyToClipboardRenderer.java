package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.InteractionComponents;

public class CopyToClipboardRenderer extends ActionableRenderer {

	public CopyToClipboardRenderer(CompilationContext context, InteractionComponents.Actionable component, RendererWriter provider) {
		super(context, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		String text = element.asCopyToClipboard().text();
		if (text != null) properties.add("text", text);
		return properties;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("operation", "");
	}
}
