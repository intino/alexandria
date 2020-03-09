package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.Template;
import io.intino.konos.model.graph.rules.Spacing;

public class TemplateRenderer extends SizedRenderer<Template> {

	public TemplateRenderer(CompilationContext compilationContext, Template component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		addSpacing(result);
		addLayout(result);
		return result;
	}

	private void addSpacing(FrameBuilder builder) {
		if (element.spacing() != Spacing.None) builder.add("spacing", element.spacing().value());
	}

	private void addLayout(FrameBuilder builder) {
		String[] layout = element.layout().stream().map(l -> l.name().toLowerCase()).toArray(String[]::new);
		builder.add("layout", layout);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("template", "");
	}
}
