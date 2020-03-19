package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.OtherComponents;
import io.intino.konos.model.graph.OtherComponents.Portal;
import io.intino.konos.model.graph.OtherComponents.ProxyStamp;
import io.intino.konos.model.graph.Service;

import static io.intino.konos.model.graph.OtherComponents.Portal.Type.Template;

public class ProxyStampRenderer extends ComponentRenderer<ProxyStamp> {

	public ProxyStampRenderer(CompilationContext compilationContext, ProxyStamp component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		String proxy = element.proxy();
		Service.UI.Use from = element.from();
		properties.add("proxyPackage", proxyStampPackage(from));
		properties.add("proxyDisplay", proxy);
		properties.add("proxyUseName", from.name().toLowerCase());
		properties.add("proxyUseUrl", useUrlFrame());
		properties.add("type", ProxyStamp.class.getSimpleName());
		return properties;
	}

	private Frame useUrlFrame() {
		FrameBuilder result = new FrameBuilder("useUrl");
		Service.UI.Use from = element.from();

		result.add(isCustomParameter(from.url()) ? "custom" : "standard");
		result.add("value", isCustomParameter(from.url()) ? customParameterValue(from.url()) : from.url());

		return result.toFrame();
	}

	private String proxyStampPackage(Service.UI.Use use) {
		return use.package$() + "." + use.name().toLowerCase() + ".box.ui.displays.templates";
	}

}
