package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.OtherComponents.Portal;
import io.intino.konos.model.graph.Service;

import static io.intino.konos.model.graph.OtherComponents.Portal.Type.Template;

public class PortalRenderer extends ComponentRenderer<Portal> {

	public PortalRenderer(CompilationContext compilationContext, Portal component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
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
		properties.add("inPackage", uiPackage(element.in()));
		element.parameterList().forEach(p -> properties.add("parameter", parameterMethodFrame(p.name(), p.value())));
		return properties;
	}

	private FrameBuilder buildMethod() {
		FrameBuilder result = addOwner(buildBaseFrame()).add("method").add(Portal.class.getSimpleName());
		result.add("to", element.to());
		if (element.type() == Template) result.add("type", element.type().name());
		result.add("inName", element.in().name$());
		result.add("inUrl", element.in().url());
		result.add("inPackage", uiPackage(element.in()));
		return result;
	}

	private String uiPackage(Service.UI.Use use) {
		return use.package$() + "." + use.name().toLowerCase() + ".box.ui";
	}

}
