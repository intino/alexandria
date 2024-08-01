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

import static io.intino.konos.builder.codegeneration.Formatters.all;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;

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
				builder.add("format", frameOf("readonly", theme.readonly().format().content(), theme.readonly().format().darkContent()));
		}
		service.graph().formatList().forEach(r -> builder.add("format", frameOf(r)));
		Commons.write(new File(gen(Target.Accessor) + File.separator + "Theme.js").toPath(), new ThemeTemplate().render(builder.toFrame(), all));
	}

	private Frame palette(Theme theme) {
		FrameBuilder result = new FrameBuilder("palette");
		result.add("type", theme.type().name());
		if (theme.primary() != null) addProperty("primary", theme.primary().color(), theme.primary().darkColor(), result);
		if (theme.secondary() != null) addProperty("secondary", theme.secondary().color(), theme.secondary().darkColor(), result);
		if (theme.error() != null) addProperty("error", theme.error().color(), theme.error().darkColor(), result);
		result.add("contrastThreshold", theme.contrastThreshold());
		result.add("tonalOffset", theme.tonalOffset());
		return result.toFrame();
	}

	private void addProperty(String name, String color, String darkColor, FrameBuilder result) {
		result.add(name, color);
		result.add("dark" + firstUpperCase(name), darkColor != null ? darkColor : color);
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
		return frameOf(format.name$(), format.content(), format.darkContent());
	}

	private Frame frameOf(String formatName, String content, String darkContent) {
		FrameBuilder result = new FrameBuilder("format");
		result.add("name", formatName);
		result.add("content", withProperties(content != null ? content : ""));
		if (darkContent != null) {
			result.add("dark");
			if (content == null || content.isEmpty()) result.add("emptyContent");
			result.add("darkContent", withProperties(darkContent));
		}
		return result.toFrame();
	}

	private String withProperties(String content) {
		return content.replaceAll("'\\$([^']*)'", "property('$1')");
	}

}
