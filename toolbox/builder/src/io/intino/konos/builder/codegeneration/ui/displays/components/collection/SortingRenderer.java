package io.intino.konos.builder.codegeneration.ui.displays.components.collection;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.CatalogComponents.Sorting;

public class SortingRenderer extends ComponentRenderer<Sorting> {

	public SortingRenderer(Settings settings, Sorting component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder frameBuilder() {
		FrameBuilder result = super.frameBuilder();
		addBinding(result);
		return result;
	}

	private void addBinding(FrameBuilder builder) {
		if (element.collection() == null) return;
		FrameBuilder result = new FrameBuilder("binding", type()).add("name", nameOf(element));
		result.add("collection", nameOf(element.collection()));
		builder.add("binding", result);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("sorting", "");
	}
}
