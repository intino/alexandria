package io.intino.konos.builder.codegeneration;

import io.intino.konos.builder.codegeneration.services.ui.ServiceListCleaner;
import io.intino.konos.model.graph.KonosGraph;

public class FullCleaner extends Cleaner {
	private final KonosGraph graph;

	public FullCleaner(Settings settings, KonosGraph graph) {
		super(settings);
		this.graph = graph;
	}

	@Override
	public void execute() {
		clean(gen(Target.Owner));
		new ServiceListCleaner(settings, graph).execute();
	}

}
