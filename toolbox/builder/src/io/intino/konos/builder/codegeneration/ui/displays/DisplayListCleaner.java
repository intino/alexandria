package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Cleaner;
import io.intino.konos.builder.codegeneration.ElementReference;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.model.graph.ui.UIService;

import java.util.List;

public class DisplayListCleaner extends Cleaner {

	public DisplayListCleaner(Settings settings, UIService service) {
		super(settings);
	}

	@Override
	public void execute() {
		List<ElementReference> removeList = cache().removeList();
		removeList.stream().filter(this::isDisplay).forEach(r -> new DisplayCleaner(settings, r).execute());
	}

	private boolean isDisplay(ElementReference reference) {
		return reference.context() == ElementReference.Context.Display;
	}
}