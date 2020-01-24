package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.CatalogComponents.DownloadCollection;

public class DownloadCollectionRenderer extends BindingCollectionRenderer<DownloadCollection> {

	public DownloadCollectionRenderer(Settings settings, DownloadCollection component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder, element.collections());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("searchbox", "");
	}

}
