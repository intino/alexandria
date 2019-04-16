package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.Template;
import io.intino.konos.model.graph.rules.Spacing;
import org.siani.itrules.model.Frame;

public class TemplateRenderer extends ComponentRenderer<Template> {

	public TemplateRenderer(Settings settings, Template component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		addSpacing(result);
		addLayout(result);
		return result;
	}

	private void addSpacing(Frame result) {
		if (element.spacing() != Spacing.None) result.addSlot("spacing", element.spacing().value());
	}

	private void addLayout(Frame result) {
		String[] layout = element.layout().stream().map(l -> l.name().toLowerCase()).toArray(String[]::new);
		result.addSlot("layout", layout);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("template", "");
	}
}
