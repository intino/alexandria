package io.intino.pandora.plugin.utils;

import com.intellij.openapi.diagnostic.Logger;
import io.intino.pandora.model.PandoraApplication;
import tara.io.Stash;
import tara.magritte.Graph;

public class GraphLoader {
	private static final Logger LOG = Logger.getInstance("GraphLoader");


	public static PandoraApplication loadGraph(Stash... stash) {
		final ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(GraphLoader.class.getClassLoader());
		final Graph graph = Graph.from(stash).wrap(PandoraApplication.class);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return graph.application();
	}

}
