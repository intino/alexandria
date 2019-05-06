package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.OtherComponents.Spinner;
import org.siani.itrules.model.Frame;

public class SpinnerRenderer extends ComponentRenderer<Spinner> {

	public SpinnerRenderer(Settings settings, Spinner component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame properties = super.properties();
		properties.addSlot("mode", element.mode().name());
		return properties;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("spinner", "");
	}
}
