package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.Template;
import io.intino.konos.dsl.rules.Spacing;

public class TemplateRenderer extends SizedRenderer<Template> {

	public TemplateRenderer(CompilationContext compilationContext, Template component, RendererWriter provider) {
		super(compilationContext, component, provider);
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
