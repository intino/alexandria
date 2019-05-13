package io.intino.konos.builder.codegeneration.ui.displays.components.operation;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.OperationRenderer;
import io.intino.konos.model.graph.OperationComponents.DownloadSelection;

public class DownloadSelectionRenderer extends OperationRenderer<DownloadSelection> {

	public DownloadSelectionRenderer(Settings settings, DownloadSelection component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder frame = super.properties();
		element.options().forEach(o -> frame.add("option", o));
		return frame;
	}

}
