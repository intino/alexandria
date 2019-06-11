package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.model.graph.OtherComponents.Dashboard;

public class DashboardRenderer extends SizedRenderer<Dashboard> {

	public DashboardRenderer(Settings settings, Dashboard component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		if (element.script() != null && !element.script().isEmpty()) result.add("script", resourceMethodFrame("script", element.script()));
		result.add("homeDirectory", element.homeDirectory());
		result.add("startPort", element.startPort());
		element.parameterList().forEach(p -> result.add("parameter", parameterFrame(p)));
		return result;
	}

	private FrameBuilder parameterFrame(Dashboard.Parameter parameter) {
		FrameBuilder frame = new FrameBuilder("parameter", "dashboard");
		frame.add("name", parameter.name());
		frame.add("value", parameter.value());
		addOwner(frame);
		return frame;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("dashboard", "");
	}
}
