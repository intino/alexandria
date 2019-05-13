package io.intino.konos.builder.codegeneration.ui.displays.components.operation;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.OperationRenderer;
import io.intino.konos.model.graph.OperationComponents.OpenPage;
import org.siani.itrules.model.Frame;

public class OpenPageRenderer extends OperationRenderer<OpenPage> {

	public OpenPageRenderer(Settings settings, OpenPage component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame properties = super.properties();
		if (element.page() != null) properties.addSlot("path", element.page().path());
		return properties;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("operation", "");
	}
}
