package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.ActionableComponents;

public class UploadRenderer extends ActionableRenderer {

	public UploadRenderer(CompilationContext context, ActionableComponents.Actionable component, RendererWriter provider) {
		super(context, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		ActionableComponents.Actionable.Upload upload = element.asUpload();
		if (upload.multipleSelection()) result.add("multipleSelection", upload.multipleSelection());
		return result;
	}

}
