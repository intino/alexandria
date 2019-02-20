package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.ThemeTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.Theme;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.model.Frame;

import java.io.File;

import static io.intino.konos.builder.helpers.Commons.write;
import static io.intino.konos.model.graph.Theme.Palette.Type.Normal;

public class ThemeRenderer extends UIRenderer {
	private final UIService service;

	protected ThemeRenderer(Settings settings, UIService service) {
		super(settings, Target.Accessor);
		this.service = service;
	}

	@Override
	public void execute() {
		Frame frame = new Frame().addTypes("theme");
		Theme theme = service.theme();
		frame.addSlot("palette", palette(theme));
		frame.addSlot("typography", typography(theme));
		theme.styleList().forEach(r -> frame.addSlot("style", frameOf(r)));
		write(new File(accessorGen() + File.separator + "Theme.js").toPath(), setup(ThemeTemplate.create()).format(frame));
	}

	private Frame palette(Theme theme) {
		Theme.Palette palette = theme.palette();
		if (palette == null) palette = theme.create().palette();
		Frame result = new Frame("palette");
		if (palette.type() != Normal) result.addSlot("type", palette.type().name());
		result.addSlot("primary", palette.primary().color());
		result.addSlot("secondary", palette.secondary().color());
		result.addSlot("error", palette.error().color());
		result.addSlot("contrastThreshold", palette.contrastThreshold());
		result.addSlot("tonalOffset", palette.tonalOffset());
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

	private Frame frameOf(Theme.Style style) {
		Frame result = new Frame().addTypes("style");
		result.addSlot("name", style.name$());
		result.addSlot("type", style.getClass().getSimpleName().toLowerCase());
		style.propertyList().forEach(p -> result.addSlot("property", frameOf(p)));
		return result;
	}

	private Frame frameOf(Theme.Style.Property property) {
		Frame result = new Frame().addTypes("property");
		result.addSlot("name", property.getClass().getSimpleName().toLowerCase());
		result.addSlot("content", property.content());
		return result;
	}
}
