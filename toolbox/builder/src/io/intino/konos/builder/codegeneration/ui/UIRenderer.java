package io.intino.konos.builder.codegeneration.ui;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.DisplayHelper;
import io.intino.tara.magritte.Layer;

import java.util.Map;

public abstract class UIRenderer extends Renderer {

	public static final String Resources = "%sresources";
	public static final String Notifiers = "%sdisplays/notifiers";
	public static final String Requesters = "%sdisplays/requesters";

	public static final String Pages = "%spages";

	protected UIRenderer(Settings settings, Target target) {
		super(settings, target);
	}

	public FrameBuilder frameBuilder() {
		return baseFrameBuilder();
	}

	protected String parent() {
		return settings.parent();
	}

	protected Map<String, String> classes() {
		return settings.classes();
	}

	protected Template setup(Template template) {
		return addFormats(template);
	}

	protected String path(io.intino.konos.model.graph.Display display) {
		return DisplayHelper.displayPath(typeOf(display), target);
	}

	protected Template addFormats(Template template) {
		Formatters.customize(template);
		return template;
	}

	protected String typeOf(Layer element) {
		return elementHelper.typeOf(element);
	}

	protected String nameOf(Layer element) {
		return elementHelper.nameOf(element);
	}

	protected String shortId(Layer element) {
		return elementHelper.shortId(element);
	}

	protected String shortId(Layer element, String suffix) {
		return elementHelper.shortId(element, suffix);
	}

}