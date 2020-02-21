package io.intino.konos.builder.codegeneration.ui.displays.components.actionable;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ActionableRenderer;
import io.intino.konos.model.graph.InteractionComponents;
import io.intino.konos.model.graph.Service;

public class OpenPageRenderer extends ActionableRenderer {

	public OpenPageRenderer(CompilationContext context, InteractionComponents.Actionable component, TemplateProvider provider, Target target) {
		super(context, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		Service.UI.Resource.Page page = element.asOpenPage().page();
		if (page != null) properties.add("path", page.path());
		return properties;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("operation", "");
	}
}
