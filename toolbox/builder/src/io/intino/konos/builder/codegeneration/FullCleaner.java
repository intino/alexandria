package io.intino.konos.builder.codegeneration;

import io.intino.konos.builder.codegeneration.services.ui.ServiceListCleaner;
import io.intino.konos.model.graph.KonosGraph;

public class FullCleaner extends Cleaner {
	private final KonosGraph graph;

	public FullCleaner(CompilationContext compilationContext, KonosGraph graph) {
		super(compilationContext);
		this.graph = graph;
	}

	@Override
	public void execute() {
		clean(gen(Target.Owner));
		new ServiceListCleaner(compilationContext, graph).execute();
	}

}
