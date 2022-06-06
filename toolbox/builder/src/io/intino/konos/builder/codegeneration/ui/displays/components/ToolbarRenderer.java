package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.CatalogComponents;
import io.intino.konos.model.InteractionComponents.Toolbar;

import java.util.Arrays;

public class ToolbarRenderer extends ComponentRenderer<Toolbar> {

	public ToolbarRenderer(CompilationContext compilationContext, Toolbar component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder);
	}

	private void addBinding(FrameBuilder builder) {
		if (!element.isLinked()) return;

		CatalogComponents.Collection collection = element.asLinked().to();
		if (collection == null) return;
		String[] ancestors = ancestors(collection);
		builder.add("binding", new FrameBuilder("binding", Toolbar.class.getSimpleName())
			 	.add("name", nameOf(element))
				.add("collection", nameOf(collection))
				.add("ancestorsNotMe", ancestors.length > 0 ? Arrays.copyOfRange(ancestors, 1, ancestors.length) : new String[0]));
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("toolbar", "");
	}
}
