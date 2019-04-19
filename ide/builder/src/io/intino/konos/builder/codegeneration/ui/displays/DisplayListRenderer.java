package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.ui.UIService;

import java.util.List;

import static io.intino.konos.model.graph.KonosGraph.displaysOf;

@SuppressWarnings("Duplicates")
public class DisplayListRenderer extends UIRenderer {
	private final List<Display> displays;
	private final TemplateProvider templateProvider;

	public DisplayListRenderer(Settings settings, UIService service, TemplateProvider templateProvider, Target target) {
		super(settings, target);
		this.displays = displaysOf(service);
		this.templateProvider = templateProvider;
	}

	public void execute() {
		DisplayRendererFactory factory = new DisplayRendererFactory();
		displays.forEach(d -> factory.renderer(settings, d, templateProvider, target).execute());
	}

}