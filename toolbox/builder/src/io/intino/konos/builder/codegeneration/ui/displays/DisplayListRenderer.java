package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.alexandria.logger.Logger;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.model.Display;
import io.intino.konos.model.Service;

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
	public void render() throws KonosException {
		DisplayRendererFactory factory = new DisplayRendererFactory();
		displays.forEach(d -> render(d, factory));
		//if (target == Target.Owner) delay();
	}

	private void delay() { // Required to avoid compilation problems
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Logger.error(e);
		}
	}

	private void render(Display display, DisplayRendererFactory factory) {
		try {
			factory.renderer(context, display, templateProvider, target).execute();
		} catch (KonosException e) {
			Logger.error(e);
		}
	}

}