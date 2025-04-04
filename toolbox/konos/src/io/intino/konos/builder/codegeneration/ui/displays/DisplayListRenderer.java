package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.alexandria.logger.Logger;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.utils.NamedThreadFactory;
import io.intino.konos.dsl.Display;
import io.intino.konos.dsl.Service;

import java.util.List;
import java.util.concurrent.*;

@SuppressWarnings("Duplicates")
public class DisplayListRenderer extends UIRenderer {
	private final List<Display> displays;
	private final RendererWriter rendererWriter;

	public DisplayListRenderer(CompilationContext compilationContext, Service.UI service, RendererWriter writer) {
		super(compilationContext);
		this.displays = service.graph().rootDisplays(compilationContext.graphName());
		this.rendererWriter = writer;
	}

	@Override
	public void render() throws KonosException {
		asyncRender();
	}

	private void asyncRender() throws KonosException {
		FrameBuilder.startCache();
		DisplayRendererFactory factory = new DisplayRendererFactory();
		ExecutorService service = Executors.newFixedThreadPool(displays.size() > 4 ? Runtime.getRuntime().availableProcessors() : 2, new NamedThreadFactory("displays"));
		try {
			List<Future<Boolean>> futures = displays.stream().<Callable>map(d -> () -> render(d, factory)).<Future<Boolean>>map(task -> service.submit(task)).toList();
			service.shutdown();
			service.awaitTermination(1, TimeUnit.HOURS);
			for (Future<Boolean> future : futures) future.get();
		} catch (Throwable e) {
			Logger.error(e);
			throw new KonosException(e.getMessage());
		}
		FrameBuilder.stopCache();
	}

	private boolean render(Display display, DisplayRendererFactory factory) throws KonosException {
		try {
			factory.renderer(context, display, rendererWriter).execute();
			FrameBuilder.clearCache();
			return true;
		} catch (Throwable e) {
			throw new KonosException(e.getClass().getSimpleName() + ":" + e.getMessage(), e);
		}
	}
}