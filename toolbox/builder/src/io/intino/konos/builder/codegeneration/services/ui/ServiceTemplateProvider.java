package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.templates.*;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.PassiveView;
import io.intino.magritte.framework.Layer;

public class ServiceTemplateProvider implements TemplateProvider {

	public Template srcTemplate(Layer layer, FrameBuilder builder) {
		if (builder.is("accessible")) return null;
		if (!ElementHelper.isRoot(layer)) return null;
		return setup(new DisplayTemplate());
	}

	public Template genTemplate(Layer layer, FrameBuilder builder) {
		if (layer.i$(io.intino.konos.model.Template.Desktop.class)) return setup(new AbstractDesktopTemplate());
		return setup(new AbstractDisplayTemplate());
	}

	public Template notifierTemplate(PassiveView element, FrameBuilder builder) {
		return setup(new PassiveViewNotifierTemplate());
	}

	public Template requesterTemplate(PassiveView element, FrameBuilder builder) {
		return setup(new PassiveViewRequesterTemplate());
	}

	public Template pushRequesterTemplate(PassiveView element, FrameBuilder builder) {
		return setup(new PassiveViewPushRequesterTemplate());
	}

	private Template setup(Template template) {
		return addFormats(template);
	}

	private Template addFormats(Template template) {
		return Formatters.customize(template);
	}

}
