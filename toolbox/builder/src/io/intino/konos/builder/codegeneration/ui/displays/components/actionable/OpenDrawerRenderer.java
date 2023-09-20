package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.ActionableComponents;
import io.intino.konos.model.Block;

public class OpenDrawerRenderer extends ActionableRenderer {

	public OpenDrawerRenderer(CompilationContext context, ActionableComponents.Actionable component, RendererWriter provider) {
		super(context, component, provider);
	}

	@Override
	public void fill(FrameBuilder builder) {
		addBinding(builder);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		Block.Drawer drawer = element.asOpenDrawer().drawer();
		if (drawer != null) properties.add("drawer", nameOf(drawer));
		return properties;
	}

	protected void addBinding(FrameBuilder builder) {
		Block.Drawer drawer = element.asOpenDrawer().drawer();
		if (drawer == null) return;
		FrameBuilder result = new FrameBuilder("binding", "opendrawer").add("name", nameOf(element));
		result.add("drawer", nameOf(drawer));
		builder.add("binding", result);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("operation", "");
	}
}
