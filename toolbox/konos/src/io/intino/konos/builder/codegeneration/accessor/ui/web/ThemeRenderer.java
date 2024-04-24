package io.intino.konos.builder.codegeneration.accessor.ui.web;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.accessor.ui.web.templates.ThemeTemplate;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.dsl.Format;
import io.intino.konos.dsl.Service;
import io.intino.konos.dsl.Theme;

import java.io.File;

import static io.intino.konos.dsl.Theme.Type.Normal;

public class ThemeRenderer extends UIRenderer {
	private final Service.UI service;

	protected ThemeRenderer(CompilationContext compilationContext, Service.UI service) {
		super(compilationContext);
		this.service = service;
	}

	@Override
	public void render() {
		FrameBuilder builder = new FrameBuilder("theme");
		Theme theme = service.graph().theme();
		if (theme != null) {
			builder.add("palette", palette(theme));
			builder.add("typography", typography(theme));
			if (theme.readonly() != null)
				builder.add("format", frameOf("readonly", theme.readonly().format().content()));
		}
		service.graph().formatList().forEach(r -> builder.add("format", frameOf(r)));
		Commons.write(new File(gen(Target.Accessor) + File.separator + "Theme.js").toPath(), setup(new ThemeTemplate()).render(builder.toFrame()));
	}

	private Frame palette(Theme theme) {
		FrameBuilder result = new FrameBuilder("palette");
		if (theme.type() != Normal) result.add("type", theme.type().name());
		if (theme.primary() != null) result.add("primary", theme.primary().color());
		if (theme.secondary() != null) result.add("secondary", theme.secondary().color());
		if (theme.error() != null) result.add("error", theme.error().color());
		result.add("contrastThreshold", theme.contrastThreshold());
		result.add("tonalOffset", theme.tonalOffset());
		return result.toFrame();
	}

	private Frame typography(Theme theme) {
		Theme.Typography typography = theme.typography();
		if (typography == null) typography = theme.create().typography();
		FrameBuilder result = new FrameBuilder("typography");
		result.add("fontFamily", "\"" + String.join("\",\"", typography.fontFamily()) + "\"");
		result.add("fontSize", typography.fontSize());
		return result.toFrame();
	}

	private Frame frameOf(Format format) {
		return frameOf(format.name$(), format.content());
	}

	private Frame frameOf(String formatName, String content) {
		FrameBuilder result = new FrameBuilder("format");
		result.add("name", formatName);
		result.add("content", content != null ? content : "");
		return result.toFrame();
	}

}
