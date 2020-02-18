package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.model.graph.VisualizationComponents.Dashboard;

public class DashboardRenderer extends SizedRenderer<Dashboard> {

	public DashboardRenderer(Settings settings, Dashboard component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		if (element.serverScript() != null && !element.serverScript().isEmpty()) result.add("serverScript", resourceMethodFrame("serverScript", element.serverScript()));
		if (element.uiScript() != null && !element.uiScript().isEmpty()) result.add("uiScript", resourceMethodFrame("uiScript", element.uiScript()));
		element.resources().forEach(r -> result.add("resource", resourceMethodFrame("add", r)));
		element.parameterList().forEach(p -> result.add("parameter", parameterMethodFrame(p.name(), p.value())));
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("dashboard", "");
	}
}
