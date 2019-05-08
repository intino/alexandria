package io.intino.konos.builder.codegeneration.ui.displays.components.operation;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.OperationRenderer;
import io.intino.konos.model.graph.OperationComponents.DownloadSelection;
import org.siani.itrules.model.Frame;

public class DownloadSelectionRenderer extends OperationRenderer<DownloadSelection> {

	public DownloadSelectionRenderer(Settings settings, DownloadSelection component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame frame = super.properties();
		element.options().forEach(o -> frame.addSlot("option", o));
		return frame;
	}

}
