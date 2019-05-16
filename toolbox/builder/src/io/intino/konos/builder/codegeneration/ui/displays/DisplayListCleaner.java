package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Cleaner;
import io.intino.konos.builder.codegeneration.ElementReference;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.ui.UIService;

import java.util.ArrayList;
import java.util.List;

import static io.intino.konos.model.graph.KonosGraph.rootDisplays;
import static java.util.stream.Collectors.toList;

public class DisplayListCleaner extends Cleaner {
	private final List<Display> displays;

	public DisplayListCleaner(Settings settings, UIService service) {
		super(settings);
		this.displays = rootDisplays(service);
	}

	@Override
	public void execute() {
		List<String> cacheReferences = new ArrayList<>(cache().keySet());
		List<String> references = displays.stream().map(d -> elementHelper.referenceOf(d).toString()).collect(toList());
		cacheReferences.stream().filter(r -> isDisplay(r) && !references.contains(r)).forEach(r -> new DisplayCleaner(settings, ElementReference.from(r)).execute());
	}

	private boolean isDisplay(String reference) {
		return ElementReference.from(reference).context() == ElementReference.Context.Display;
	}
}