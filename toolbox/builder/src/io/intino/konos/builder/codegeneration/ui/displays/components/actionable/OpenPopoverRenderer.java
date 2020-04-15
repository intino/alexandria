package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.Block;
import io.intino.konos.model.graph.InteractionComponents;
import io.intino.konos.model.graph.OtherComponents;

public class OpenPopoverRenderer extends ActionableRenderer {

	public OpenPopoverRenderer(CompilationContext context, InteractionComponents.Actionable component, TemplateProvider provider, Target target) {
		super(context, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder);
	}

	protected void addBinding(FrameBuilder builder) {
		Block.Popover popover = element.asOpenPopover().popover();
		if (popover == null) return;
		FrameBuilder result = new FrameBuilder("binding", "openpopover").add("name", nameOf(element));
		result.add("popover", nameOf(popover));
		builder.add("binding", result);
	}

}
