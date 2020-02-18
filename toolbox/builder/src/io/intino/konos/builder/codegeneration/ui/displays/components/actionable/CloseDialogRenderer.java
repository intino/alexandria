package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.model.graph.InteractionComponents;

public class CloseDialogRenderer extends ActionableRenderer {

	public CloseDialogRenderer(Settings settings, InteractionComponents.Actionable component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder);
	}

	protected void addBinding(FrameBuilder builder) {
		FrameBuilder result = new FrameBuilder("binding", "closedialog").add("name", nameOf(element));
		result.add("dialog", nameOf(element.asCloseDialog().dialog()));
		builder.add("binding", result);
	}

}
