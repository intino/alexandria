package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.ChildComponents.Date;
import org.siani.itrules.model.Frame;

public class DateRenderer extends ComponentRenderer<Date> {

	public DateRenderer(Settings settings, Date component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		result.addSlot("format", element.format());
		if (element.mode() != Date.Mode.None) result.addSlot("mode", element.mode().name().toLowerCase());
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("header", "");
	}
}
