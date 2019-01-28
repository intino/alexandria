package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.AbstractDesktopTemplate;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.AbstractDisplayTemplate;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.DisplayTemplate;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.PassiveViewNotifierTemplate;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.PassiveViewRequesterTemplate;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.Desktop;
import io.intino.konos.model.graph.PassiveView;
import io.intino.konos.model.graph.decorated.DecoratedDisplay;
import io.intino.tara.magritte.Layer;
import org.siani.itrules.Template;

public class AccessorTemplateProvider implements TemplateProvider {

	public Template srcTemplate(Layer layer) {
		if (!layer.i$(DecoratedDisplay.class)) return null;
		return setup(DisplayTemplate.create());
	}

	public Template genTemplate(Layer layer) {
		if (layer.i$(Desktop.class)) return setup(AbstractDesktopTemplate.create());
		return setup(AbstractDisplayTemplate.create());
	}

	public Template notifierTemplate(PassiveView element) {
		return setup(PassiveViewNotifierTemplate.create());
	}

	public Template requesterTemplate(PassiveView element) {
		return setup(PassiveViewRequesterTemplate.create());
	}

	private Template setup(Template template) {
		return addFormats(template);
	}

	private Template addFormats(Template template) {
		Formatters.customize(template);
		return template;
	}

}
