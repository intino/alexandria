package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.ChildComponents.Text;
import io.intino.konos.model.graph.code.childcomponents.CodeText;
import org.siani.itrules.model.Frame;

public class TextRenderer extends ComponentRenderer<Text> {

	public TextRenderer(Settings settings, Text component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame();
		if (element.isCode()) {
			Frame codeFrame = new Frame(CodeText.class.getSimpleName());
			if (element.asCode().value() != null) codeFrame.addSlot("value", element.asCode().value().trim());
			frame.addSlot("code", codeFrame);
		}
		return frame;
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
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

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("text", "");
	}
}
