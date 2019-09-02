package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.OtherComponents.Portal;

import static io.intino.konos.model.graph.OtherComponents.Portal.Type.Template;

public class PortalRenderer extends ComponentRenderer<Portal> {

	public PortalRenderer(Settings settings, Portal component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	protected void fill(FrameBuilder builder) {
		super.fill(builder);
		builder.add("methods", buildMethod());
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		properties.add("to", element.to());
		properties.add("inUrl", element.in().url());
		properties.add("inName", element.in().name$());
		properties.add("inPackage", packageOf(element.in().className()));
		element.parameterList().forEach(p -> properties.add("parameter", parameterMethodFrame(p.name(), p.value())));
		return properties;
	}

	private FrameBuilder buildMethod() {
		FrameBuilder result = addOwner(buildBaseFrame()).add("method").add(Portal.class.getSimpleName());
		result.add("to", element.to());
		if (element.type() == Template) result.add("type", element.type().name());
		result.add("inName", element.in().name$());
		result.add("inUrl", element.in().url());
		result.add("inPackage", packageOf(element.in().className()));
		return result;
	}

	private String packageOf(String className) {
		if (className.lastIndexOf(".") == -1) return className;
		return className.substring(0, className.lastIndexOf("."));
	}

}
