package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.konos.builder.codegeneration.Cleaner;
import io.intino.konos.builder.codegeneration.ElementReference;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.model.graph.Service;

import java.util.List;

public class DisplayListCleaner extends Cleaner {

	public DisplayListCleaner(CompilationContext compilationContext, Service.UI service) {
		super(compilationContext);
	}

	@Override
	public void execute() {
		List<ElementReference> removeList = cache().removeList();
		removeList.stream().filter(this::isDisplay).forEach(r -> new DisplayCleaner(compilationContext, r).execute());
	}

	private boolean isDisplay(ElementReference reference) {
		return reference.context() == ElementReference.Context.Display;
	}
}