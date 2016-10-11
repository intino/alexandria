package io.intino.pandora.plugin.actions;

import com.intellij.openapi.diagnostic.Logger;
import io.intino.pandora.plugin.PandoraApplication;
import tara.io.Stash;
import tara.magritte.Graph;

class GraphLoader {
	private static final Logger LOG = Logger.getInstance("GraphLoader");


	static PandoraApplication loadGraph(Stash... stash) {
		final ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(GraphLoader.class.getClassLoader());
		final Graph graph = Graph.from(stash).wrap(PandoraApplication.class);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return graph.application();
	}

}
