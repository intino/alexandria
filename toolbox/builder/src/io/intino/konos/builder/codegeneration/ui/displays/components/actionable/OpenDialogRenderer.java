package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.model.graph.InteractionComponents;
import io.intino.konos.model.graph.OtherComponents;

public class OpenDialogRenderer extends ActionableRenderer {

	public OpenDialogRenderer(CompilationContext context, InteractionComponents.Actionable component, TemplateProvider provider, Target target) {
		super(context, component, provider, target);
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
