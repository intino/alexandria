package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.OtherComponents.MicroSite;

public class MicroSiteRenderer extends ComponentRenderer<MicroSite> {

	public MicroSiteRenderer(CompilationContext compilationContext, MicroSite component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		if (element.site() != null) properties.add("site", resourceMethodFrame("site", element.site()));
		element.downloadOperations().forEach(o -> properties.add("downloadOperation", o.name()));
		return properties;
	}

}
