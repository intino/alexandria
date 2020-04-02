package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.InteractionComponents;

public class DownloadRenderer extends ActionableRenderer {

	public DownloadRenderer(CompilationContext context, InteractionComponents.Actionable component, TemplateProvider provider, Target target) {
		super(context, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder, element.asDownload().collection());
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		InteractionComponents.Actionable.Download download = element.asDownload();
		download.options().forEach(o -> result.add("option", o));
		if (download.context() == InteractionComponents.Actionable.Download.Context.Selection) result.add("selection");
		return result;
	}

	private void addBinding(FrameBuilder builder, CatalogComponents.Collection collection) {
		if (collection == null) return;
		FrameBuilder result = new FrameBuilder("binding", type()).add("name", nameOf(element));
		result.add("collection", nameOf(collection));
		builder.add("binding", result);
	}

}
