package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.alexandria.logger.Logger;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.utils.NamedThreadFactory;
import io.intino.konos.model.Display;
import io.intino.konos.model.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
		asyncRender();
	}

	private void asyncRender() {
		FrameBuilder.startCache();
		DisplayRendererFactory factory = new DisplayRendererFactory();
		ExecutorService service = Executors.newFixedThreadPool(displays.size() > 4 ? Runtime.getRuntime().availableProcessors() : 2, new NamedThreadFactory("displays"));
		displays.stream().<Runnable>map(d -> () -> render(d, factory)).forEach(service::execute);
		try {
			service.shutdown();
			service.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			Logger.error(e);
		}
		FrameBuilder.stopCache();
	}

	private void render(Display display, DisplayRendererFactory factory) {
		try {
			factory.renderer(context, display, templateProvider, target).execute();
			FrameBuilder.clearCache();
		} catch (KonosException e) {
			Logger.error(e);
		}
	}
}