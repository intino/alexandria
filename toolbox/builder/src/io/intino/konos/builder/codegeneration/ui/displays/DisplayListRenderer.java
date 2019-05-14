package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.ElementReference;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.ui.UIService;

import java.util.ArrayList;
import java.util.List;

import static io.intino.konos.model.graph.KonosGraph.displaysOf;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("Duplicates")
public class DisplayListRenderer extends UIRenderer {
	private final List<Display> displays;
	private final TemplateProvider templateProvider;

	public DisplayListRenderer(Settings settings, UIService service, TemplateProvider templateProvider, Target target) {
		super(settings, target);
		this.displays = displaysOf(service);
		this.templateProvider = templateProvider;
	}

	@Override
	public void clean() {
		List<ElementReference> cacheReferences = new ArrayList<>(cache().keySet());
		DisplayListCleaner garbageCollector = new DisplayListCleaner();
		List<String> references = displays.stream().map(d -> ElementReference.of(nameOf(d), typeOf(d)).toString()).collect(toList());
		cacheReferences.stream().filter(r -> !references.contains(r.toString())).forEach(r -> garbageCollector.execute(r));
	}

	@Override
	public void render() {
		DisplayRendererFactory factory = new DisplayRendererFactory();
		displays.forEach(d -> factory.renderer(settings, d, templateProvider, target).execute());
	}

}