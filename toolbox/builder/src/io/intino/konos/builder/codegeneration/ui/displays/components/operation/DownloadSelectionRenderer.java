package io.intino.konos.builder.codegeneration.ui.displays.components.operation;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.OperationRenderer;
import io.intino.konos.model.graph.OperationComponents.DownloadSelection;

public class DownloadSelectionRenderer extends OperationRenderer<DownloadSelection> {

	public DownloadSelectionRenderer(CompilationContext compilationContext, DownloadSelection component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder frame = super.properties();
		element.options().forEach(o -> frame.add("option", o));
		return frame;
	}

}
