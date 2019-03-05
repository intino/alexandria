package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.ChildComponents.Header;
import org.siani.itrules.model.Frame;

public class HeaderRenderer extends ComponentRenderer<Header> {

	public HeaderRenderer(Settings settings, Header component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		result.addSlot("color", element.color().name().toLowerCase());
		result.addSlot("position", element.position().name().toLowerCase());
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("header", "");
	}
}
