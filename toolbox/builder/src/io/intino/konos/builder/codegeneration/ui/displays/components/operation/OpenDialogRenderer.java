package io.intino.konos.builder.codegeneration.ui.displays.components.operation;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.OperationRenderer;
import io.intino.konos.model.graph.OperationComponents.OpenDialog;

public class OpenDialogRenderer extends OperationRenderer<OpenDialog> {

	public OpenDialogRenderer(Settings settings, OpenDialog component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder frameBuilder() {
		FrameBuilder result = super.frameBuilder();
		addBinding(result);
		return result;
	}

	protected void addBinding(FrameBuilder builder) {
		FrameBuilder result = new FrameBuilder("binding", "opendialog").add("name", nameOf(element));
		result.add("dialog", nameOf(element.dialog()));
		builder.add("binding", result);
	}

}
