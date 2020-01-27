package io.intino.konos.builder.codegeneration.ui.displays.components.operation;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.OperationRenderer;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.OperationComponents.Download;

public class DownloadRenderer extends OperationRenderer<Download> {

	public DownloadRenderer(Settings settings, Download component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder, element.collection());
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		element.options().forEach(o -> result.add("option", o));
		return result;
	}

	private void addBinding(FrameBuilder builder, CatalogComponents.Collection collection) {
		if (collection == null) return;
		FrameBuilder result = new FrameBuilder("binding", type()).add("name", nameOf(element));
		result.add("collection", nameOf(collection));
		builder.add("binding", result);
	}

}
