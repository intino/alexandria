package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.DataComponents.Text;
import io.intino.konos.model.graph.code.datacomponents.CodeText;
import io.intino.konos.model.graph.editable.datacomponents.EditableText;
import io.intino.konos.model.graph.highlighted.datacomponents.HighlightedText;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

public class TextRenderer extends ComponentRenderer<Text> {

	public TextRenderer(Settings settings, Text component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public void fill(FrameBuilder builder) {
		if (element.prefix() != null) builder.add("prefix", element.prefix());
		if (element.suffix() != null) builder.add("suffix", element.suffix());
		if (element.isCode()) {
			FrameBuilder codeFrame = new FrameBuilder(CodeText.class.getSimpleName());
			if (element.asCode().value() != null) codeFrame.add("value", escape(element.asCode().value()));
			builder.add("code", codeFrame);
		}
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
		if (element.isCode()) {
			result.add(CodeText.class.getSimpleName());
			result.add("language", element.asCode().language().name());
		}
		if (element.value() != null) {
			String value = element.isCode() ? element.value().replaceAll("\\n", "").replaceAll("\"", "\\\\\"") : element.value();
			result.add("defaultValue", value);
		}
		if (element.isEditable()) {
			EditableText editableText = element.asEditable();
			if (editableText.placeHolder() != null) result.add("placeholder", editableText.placeHolder());
		}
		return result;
	}

	private void addHighlight(FrameBuilder result) {
		if (!element.isHighlighted()) return;
		HighlightedText highlight = element.asHighlighted();
		FrameBuilder highlightedFrame = new FrameBuilder("highlighted").add("text", highlight.textColor()).add("background", highlight.backgroundColor());
		result.add("highlighted", highlightedFrame);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("text", "");
	}
}
