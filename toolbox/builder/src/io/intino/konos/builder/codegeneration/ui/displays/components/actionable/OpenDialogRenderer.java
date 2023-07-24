package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.ActionableComponents;
import io.intino.konos.model.InteractionComponents;
import io.intino.konos.model.OtherComponents;

public class OpenDialogRenderer extends ActionableRenderer {

	public OpenDialogRenderer(CompilationContext context, ActionableComponents.Actionable component, RendererWriter provider) {
		super(context, component, provider);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder);
	}

	protected void addBinding(FrameBuilder builder) {
		OtherComponents.AbstractDialog dialog = element.asOpenDialog().dialog();
		if (dialog == null) return;
		FrameBuilder result = new FrameBuilder("binding", "opendialog").add("name", nameOf(element));
		result.add("dialog", nameOf(dialog));
		builder.add("binding", result);
	}

}
