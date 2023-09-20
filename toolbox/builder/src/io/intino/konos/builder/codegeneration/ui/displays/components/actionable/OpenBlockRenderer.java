package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.ActionableComponents;

public class OpenBlockRenderer extends ActionableRenderer {

	public OpenBlockRenderer(CompilationContext context, ActionableComponents.Actionable component, RendererWriter provider) {
		super(context, component, provider);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder);
	}

	protected void addBinding(FrameBuilder builder) {
		FrameBuilder result = new FrameBuilder("binding", "openblock").add("name", nameOf(element));
		result.add("block", nameOf(element.asOpenBlock().block()));
		builder.add("binding", result);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("operation", "");
	}
}
