package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.OtherComponents.LibraryTemplateStamp;

public class LibraryTemplateStampRenderer extends ComponentRenderer<LibraryTemplateStamp> {

	public LibraryTemplateStampRenderer(CompilationContext compilationContext, LibraryTemplateStamp component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();
		String template = element.template();
		if (template != null) properties.add("template", template);
		String path = element.path();
		if (path != null) properties.add("path", path);
		return properties;
	}

}
