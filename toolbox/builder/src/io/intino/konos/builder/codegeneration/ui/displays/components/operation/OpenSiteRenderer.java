package io.intino.konos.builder.codegeneration.ui.displays.components.operation;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.OperationRenderer;
import io.intino.konos.model.graph.OperationComponents.OpenSite;

public class OpenSiteRenderer extends OperationRenderer<OpenSite> {

	public OpenSiteRenderer(Settings settings, OpenSite component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		if (element.site() != null) properties.add("site", element.site());
		return properties;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("operation", "");
	}
}
