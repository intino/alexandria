package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.ActionableComponents;

public class OpenSiteRenderer extends ActionableRenderer {

	public OpenSiteRenderer(CompilationContext context, ActionableComponents.Actionable component, RendererWriter provider) {
		super(context, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		String site = element.asOpenSite().site();
		if (site != null) properties.add("site", site);
		return properties;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("operation", "");
	}
}
