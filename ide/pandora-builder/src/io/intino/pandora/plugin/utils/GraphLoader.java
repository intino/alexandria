package io.intino.pandora.plugin.utils;

import com.intellij.openapi.diagnostic.Logger;
import io.intino.pandora.model.PandoraApplication;
import io.intino.tara.io.Stash;
import io.intino.tara.magritte.Graph;

public class GraphLoader {
	private static final Logger LOG = Logger.getInstance("GraphLoader");

	public static PandoraApplication loadGraph(Stash... stash) {
		final ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(GraphLoader.class.getClassLoader());
		final Graph graph = Graph.use(PandoraApplication.class, null).loadStashes(stash);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return graph.application();
	}

}
