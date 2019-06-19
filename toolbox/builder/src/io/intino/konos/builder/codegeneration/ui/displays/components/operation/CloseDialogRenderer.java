package io.intino.konos.builder.codegeneration.ui.displays.components.operation;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.OperationRenderer;
import io.intino.konos.model.graph.OperationComponents.CloseDialog;

public class CloseDialogRenderer extends OperationRenderer<CloseDialog> {

	public CloseDialogRenderer(Settings settings, CloseDialog component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder);
	}

	protected void addBinding(FrameBuilder builder) {
		FrameBuilder result = new FrameBuilder("binding", "closedialog").add("name", nameOf(element));
		result.add("dialog", nameOf(element.dialog()));
		builder.add("binding", result);
	}

}
