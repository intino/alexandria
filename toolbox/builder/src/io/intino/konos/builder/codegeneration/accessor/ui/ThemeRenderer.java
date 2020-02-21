package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.ThemeTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Format;
import io.intino.konos.model.graph.Service;
import io.intino.konos.model.graph.Theme;

import java.io.File;

import static io.intino.konos.model.graph.Theme.Type.Normal;

public class ThemeRenderer extends UIRenderer {
	private final Service.UI service;

	protected ThemeRenderer(CompilationContext compilationContext, Service.UI service) {
		super(compilationContext, Target.Accessor);
		this.service = service;
	}

	@Override
	public void render() {
		FrameBuilder builder = new FrameBuilder("theme");
		Theme theme = service.graph().theme();
		if (theme != null) {
			builder.add("palette", palette(theme));
			builder.add("typography", typography(theme));
		}
		service.graph().formatList().forEach(r -> builder.add("format", frameOf(r)));
		Commons.write(new File(gen() + File.separator + "Theme.js").toPath(), setup(new ThemeTemplate()).render(builder.toFrame()));
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
		FrameBuilder result = new FrameBuilder("format");
		result.add("name", format.name$());
		result.add("content", format.content() != null ? format.content() : "");
		return result.toFrame();
	}

}
