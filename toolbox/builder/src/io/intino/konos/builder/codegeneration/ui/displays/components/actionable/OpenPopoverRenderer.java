package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.ActionableComponents;
import io.intino.konos.model.Block;
import io.intino.konos.model.InteractionComponents;

public class OpenPopoverRenderer extends ActionableRenderer {

	public OpenPopoverRenderer(CompilationContext context, ActionableComponents.Actionable component, RendererWriter provider) {
		super(context, component, provider);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		properties.add("triggerEvent", element.asOpenPopover().triggerEvent().name());
		return properties;
	}

	protected void addBinding(FrameBuilder builder) {
		Block.Popover popover = element.asOpenPopover().popover();
		if (popover == null) return;
		FrameBuilder result = new FrameBuilder("binding", "openpopover").add("name", nameOf(element));
		result.add("popover", nameOf(popover));
		builder.add("binding", result);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("operation", "");
	}
}
