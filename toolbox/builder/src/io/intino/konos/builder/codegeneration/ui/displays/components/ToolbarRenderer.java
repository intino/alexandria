package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.CatalogComponents;
import io.intino.konos.model.graph.OperationComponents.Toolbar;

public class ToolbarRenderer extends ComponentRenderer<Toolbar> {

	public ToolbarRenderer(Settings settings, Toolbar component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder frameBuilder() {
		FrameBuilder result = super.frameBuilder();
		addBinding(result);
		return result;
	}

	private void addBinding(FrameBuilder builder) {
		if (!element.isLinked()) return;

		CatalogComponents.Collection collection = element.asLinked().to();
		if (collection == null) return;

		builder.add("binding", new FrameBuilder("binding", Toolbar.class.getSimpleName())
			 	.add("name", nameOf(element))
				.add("collection", nameOf(collection)));
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("toolbar", "");
	}
}
