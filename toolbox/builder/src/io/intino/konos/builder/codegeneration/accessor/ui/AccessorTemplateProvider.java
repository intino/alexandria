package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.*;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.PassiveView;
import io.intino.konos.model.graph.decorated.DecoratedDisplay;
import io.intino.konos.model.graph.desktop.DesktopTemplate;
import io.intino.tara.magritte.Layer;

public class AccessorTemplateProvider implements TemplateProvider {

	public Template srcTemplate(Layer layer) {
		if (!layer.i$(DecoratedDisplay.class)) return null;
		return setup(new DisplayTemplate());
	}

	public Template genTemplate(Layer layer) {
		if (layer.i$(DesktopTemplate.class)) return setup(new AbstractDesktopTemplate());
		return setup(new AbstractDisplayTemplate());
	}

	public Template notifierTemplate(PassiveView element) {
		return setup(new PassiveViewNotifierTemplate());
	}

	public Template requesterTemplate(PassiveView element) {
		return setup(new PassiveViewRequesterTemplate());
	}

	public Template pushRequesterTemplate(PassiveView element) {
		return null;
	}

	private Template setup(Template template) {
		return addFormats(template);
	}

	private Template addFormats(Template template) {
		Formatters.customize(template);
		return template;
	}

}
