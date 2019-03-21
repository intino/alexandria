package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.ChildComponents.OpenPage;
import org.siani.itrules.model.Frame;

public class OpenPageRenderer extends OperationRenderer<OpenPage> {

	public OpenPageRenderer(Settings settings, OpenPage component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame properties = super.properties();
		properties.addSlot("path", element.page().path());
		return properties;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("operation", "");
	}
}
