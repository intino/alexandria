package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.ChildComponents.Text;
import org.siani.itrules.model.Frame;

public class TextRenderer extends ComponentRenderer<Text> {

	public TextRenderer(Settings settings, Text component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	protected Frame properties() {
		Frame result = super.properties();
		result.addSlot("format", element.format().name().toLowerCase());
		result.addSlot("mode", element.mode().name().toLowerCase());
		if (element.value() != null) result.addSlot("defaultValue", element.value());
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("text", "");
	}
}
