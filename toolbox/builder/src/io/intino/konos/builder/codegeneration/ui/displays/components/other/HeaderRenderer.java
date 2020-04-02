package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.OtherComponents.Header;

public class HeaderRenderer extends SizedRenderer<Header> {

	public HeaderRenderer(CompilationContext compilationContext, Header component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		result.add("position", element.position().name().toLowerCase());
		result.add("elevation", element.elevation());
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("header", "");
	}
}
