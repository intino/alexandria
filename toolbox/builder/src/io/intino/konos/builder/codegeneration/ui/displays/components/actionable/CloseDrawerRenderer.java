package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.model.graph.Block;
import io.intino.konos.model.graph.InteractionComponents;

public class CloseDrawerRenderer extends ActionableRenderer {

	public CloseDrawerRenderer(CompilationContext context, InteractionComponents.Actionable component, TemplateProvider provider, Target target) {
		super(context, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		Block.Drawer drawer = element.asCloseDrawer().drawer();
		if (drawer != null) properties.add("drawer", nameOf(drawer));
		return properties;
	}

	protected void addBinding(FrameBuilder builder) {
		Block.Drawer drawer = element.asCloseDrawer().drawer();
		if (drawer == null) return;
		FrameBuilder result = new FrameBuilder("binding", "closedrawer").add("name", nameOf(element));
		result.add("drawer", nameOf(drawer));
		builder.add("binding", result);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("operation", "");
	}
}
