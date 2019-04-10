package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.ChildComponents.Text;
import io.intino.konos.model.graph.code.childcomponents.CodeText;
import io.intino.konos.model.graph.highlight.childcomponents.HighlightText;
import org.siani.itrules.model.Frame;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

public class TextRenderer extends ComponentRenderer<Text> {

	public TextRenderer(Settings settings, Text component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame();
		if (element.prefix() != null) frame.addSlot("prefix", element.prefix());
		if (element.suffix() != null) frame.addSlot("suffix", element.suffix());
		if (element.isCode()) {
			Frame codeFrame = new Frame(CodeText.class.getSimpleName());
			if (element.asCode().value() != null) codeFrame.addSlot("value", escape(element.asCode().value()));
			frame.addSlot("code", codeFrame);
		}
		return frame;
	}

	private String escape(String value) {
		return escapeHtml(value).trim().replaceAll("\"", "'");
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		addHighlight(result);
		result.addSlot("mode", element.mode().name().toLowerCase());
		if (element.isCode()) {
			result.addTypes(CodeText.class.getSimpleName());
			result.addSlot("language", element.asCode().language().name());
		}
		if (element.value() != null) {
			String value = element.isCode() ? element.value().replaceAll("\\n", "").replaceAll("\"", "\\\\\"") : element.value();
			result.addSlot("defaultValue", value);
		}
		return result;
	}

	private void addHighlight(Frame result) {
		if (!element.isHighlight()) return;
		HighlightText highlight = element.asHighlight();
		Frame highlightFrame = new Frame("highlight").addSlot("text", highlight.textColor()).addSlot("background", highlight.backgroundColor());
		result.addSlot("highlight", highlightFrame);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("text", "");
	}
}
