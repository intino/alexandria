package io.intino.konos.builder.codegeneration.ui.displays.components.operation;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.OperationRenderer;
import io.intino.konos.model.graph.OperationComponents.OpenDialog;

public class OpenDialogRenderer extends OperationRenderer<OpenDialog> {

	public OpenDialogRenderer(CompilationContext compilationContext, OpenDialog component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder);
	}

	protected void addBinding(FrameBuilder builder) {
		if (element.dialog() == null) return;
		FrameBuilder result = new FrameBuilder("binding", "opendialog").add("name", nameOf(element));
		result.add("dialog", nameOf(element.dialog()));
		builder.add("binding", result);
	}

}
