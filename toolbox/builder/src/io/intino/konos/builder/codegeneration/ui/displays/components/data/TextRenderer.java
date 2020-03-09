package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.DataComponents.Text;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

public class TextRenderer extends ComponentRenderer<Text> {

	public TextRenderer(CompilationContext compilationContext, Text component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		if (element.prefix() != null) builder.add("prefix", element.prefix());
		if (element.suffix() != null) builder.add("suffix", element.suffix());
		if (element.isCode()) {
			FrameBuilder codeFrame = new FrameBuilder(Text.Code.class.getSimpleName());
			if (element.asCode().value() != null) codeFrame.add("value", escape(element.asCode().value()));
			builder.add("code", codeFrame);
		}
		addHighlightMethods(builder);
	}

	private String escape(String value) {
		return escapeHtml(value).trim().replaceAll("\"", "'");
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		addHighlight(result);
		result.add("mode", element.mode().name().toLowerCase());
		if (element.isPassword()) result.add("type", "password");
		if (element.isReadonly()) result.add("readonly", element.isReadonly());
		if (element.isFocused()) result.add("focused", element.isFocused());
		if (element.cropWithEllipsis() != -1) result.add("cropWithEllipsis", element.cropWithEllipsis());
		if (element.isCode()) {
			result.add(Text.Code.class.getSimpleName());
			result.add("language", element.asCode().language().name());
		}
		if (element.value() != null) {
			String value = element.isCode() ? element.value().replaceAll("\\n", "").replaceAll("\"", "\\\\\"") : element.value();
			result.add("defaultValue", value);
		}
		if (element.isEditable()) {
			Text.Editable editableText = element.asEditable();
			if (element.isMemo()) {
				result.add("editionMode", element.asMemo().editionMode().name());
				result.add("rows", element.asMemo().height());
			}
			if (editableText.placeholder() != null) result.add("placeholder", editableText.placeholder());
		}
		return result;
	}

	private void addHighlight(FrameBuilder result) {
		if (!element.isHighlighted()) return;
		Text.Highlighted highlight = element.asHighlighted();
		FrameBuilder highlightedFrame = new FrameBuilder("highlighted").add("text", highlight.textColor()).add("background", highlight.backgroundColor());
		result.add("highlighted", highlightedFrame);
	}

	private void addHighlightMethods(FrameBuilder builder) {
		if (!element.isHighlighted()) return;
		if (element.isMultiple()) return;
		FrameBuilder result = addOwner(buildBaseFrame()).add("method").add(Text.Highlighted.class.getSimpleName());
		result.add("name", nameOf(element));
		builder.add("methods", result);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("text", "");
	}
}
