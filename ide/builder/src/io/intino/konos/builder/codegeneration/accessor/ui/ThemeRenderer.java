package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.ThemeTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.Format;
import io.intino.konos.model.graph.Theme;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.model.Frame;

import java.io.File;

import static io.intino.konos.builder.helpers.Commons.write;
import static io.intino.konos.model.graph.Theme.Type.Normal;

public class ThemeRenderer extends UIRenderer {
	private final UIService service;

	protected ThemeRenderer(Settings settings, UIService service) {
		super(settings, Target.Accessor);
		this.service = service;
	}

	@Override
	public void execute() {
		Frame frame = new Frame().addTypes("theme");
		Theme theme = service.graph().theme();
		frame.addSlot("palette", palette(theme));
		frame.addSlot("typography", typography(theme));
		service.graph().formatList().forEach(r -> frame.addSlot("format", frameOf(r)));
		write(new File(accessorGen() + File.separator + "Theme.js").toPath(), setup(ThemeTemplate.create()).format(frame));
	}

	private Frame palette(Theme theme) {
		Frame result = new Frame("palette");
		if (theme.type() != Normal) result.addSlot("type", theme.type().name());
		if (theme.primary() != null) result.addSlot("primary", theme.primary().color());
		if (theme.secondary() != null) result.addSlot("secondary", theme.secondary().color());
		if (theme.error() != null) result.addSlot("error", theme.error().color());
		result.addSlot("contrastThreshold", theme.contrastThreshold());
		result.addSlot("tonalOffset", theme.tonalOffset());
		return result;
	}

	private Frame typography(Theme theme) {
		Theme.Typography typography = theme.typography();
		if (typography == null) typography = theme.create().typography();
		Frame result = new Frame("typography");
		result.addSlot("fontFamily", "\"" + String.join("\",\"", typography.fontFamily()) + "\"");
		result.addSlot("fontSize", typography.fontSize());
		return result;
	}

	private Frame frameOf(Format format) {
		Frame result = new Frame().addTypes("format");
		result.addSlot("name", format.name$());
		result.addSlot("type", format.getClass().getSimpleName().toLowerCase());
		format.propertyList().forEach(p -> result.addSlot("property", frameOf(p)));
		return result;
	}

	private Frame frameOf(Format.Property property) {
		Frame result = new Frame().addTypes("property");
		result.addSlot("name", property.getClass().getSimpleName().toLowerCase());
		result.addSlot("content", property.content());
		return result;
	}
}
