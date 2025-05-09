package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.Service;

import static io.intino.konos.dsl.OtherComponents.ExternalTemplateStamp;

public class ExternalTemplateStampRenderer extends ComponentRenderer<ExternalTemplateStamp> {

	public ExternalTemplateStampRenderer(CompilationContext compilationContext, ExternalTemplateStamp component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		String proxy = element.proxy();
		Service.UI.Use from = element.from();
		if (from != null) properties.add("proxyPackage", proxyPackage(from));
		properties.add("proxyDisplay", proxy != null ? Formatters.firstUpperCase(proxy) : " io.intino.alexandria.ui.displays.ProxyStamp");
		properties.add("proxyUseName", from != null ? from.name().toLowerCase() : "");
		properties.add("proxyUseUrl", useUrlFrame());
		if (from != null && from.socketPath() != null) properties.add("proxyUseSocketPath", from.socketPath());
		properties.add("type", ExternalTemplateStamp.class.getSimpleName());
		element.parameterList().forEach(p -> properties.add("parameter", parameterMethodFrame(p.name(), p.value())));
		return properties;
	}

	private Frame useUrlFrame() {
		FrameBuilder result = new FrameBuilder("useUrl");
		Service.UI.Use from = element.from();
		if (from == null) return result.toFrame();
		result.add(isCustomParameter(from.url()) ? "custom" : "standard");
		result.add("value", isCustomParameter(from.url()) ? customParameterValue(from.url()) : from.url());
		return result.toFrame();
	}

	private String proxyPackage(Service.UI.Use use) {
		if (use == null) return "";
		return use.package$() + ".box.ui.displays.templates";
	}

}
