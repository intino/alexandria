package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.Components.Text;
import io.intino.konos.model.graph.capitalize.components.CapitalizeText;
import io.intino.konos.model.graph.lowercase.components.LowercaseText;
import io.intino.konos.model.graph.title1.components.Title1Text;
import io.intino.konos.model.graph.title2.components.Title2Text;
import io.intino.konos.model.graph.title3.components.Title3Text;
import io.intino.konos.model.graph.uppercase.components.UppercaseText;
import org.siani.itrules.model.Frame;

public class TextRenderer extends ComponentRenderer<Text> {

	public TextRenderer(Settings settings, Text component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	protected Frame properties() {
		Frame result = super.properties();
		addFormat(result);
		addMode(result);
		return result;
	}

	private void addFormat(Frame result) {
		if (element.isTitle1()) result.addSlot("format", className(Title1Text.class));
		else if (element.isTitle2()) result.addSlot("format", className(Title2Text.class));
		else if (element.isTitle3()) result.addSlot("format", className(Title3Text.class));
	}

	private void addMode(Frame result) {
		if (element.isUppercase()) result.addSlot("mode", className(UppercaseText.class));
		else if (element.isLowercase()) result.addSlot("mode", className(LowercaseText.class));
		else if (element.isCapitalize()) result.addSlot("mode", className(CapitalizeText.class));
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("text", "");
	}
}
