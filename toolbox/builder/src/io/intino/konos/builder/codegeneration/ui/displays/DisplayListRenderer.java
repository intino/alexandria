package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.Service;

import java.util.List;

@SuppressWarnings("Duplicates")
public class DisplayListRenderer extends UIRenderer {
	private final List<Display> displays;
	private final TemplateProvider templateProvider;

	public DisplayListRenderer(CompilationContext compilationContext, Service.UI service, TemplateProvider templateProvider, Target target) {
		super(compilationContext, target);
		this.displays = service.graph().rootDisplays(compilationContext.graphName());
		this.templateProvider = templateProvider;
	}

	@Override
	public void render() {
		DisplayRendererFactory factory = new DisplayRendererFactory();
		displays.forEach(d -> factory.renderer(context, d, templateProvider, target).execute());
	}

}