package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.OtherComponents;
import io.intino.konos.model.graph.OtherComponents.Frame;
import io.intino.konos.model.graph.OtherComponents.MicroSite;

public class MicroSiteRenderer extends ComponentRenderer<MicroSite> {

	public MicroSiteRenderer(CompilationContext compilationContext, MicroSite component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		if (element.site() != null) properties.add("site", resourceMethodFrame("site", element.site()));
		return properties;
	}

}
