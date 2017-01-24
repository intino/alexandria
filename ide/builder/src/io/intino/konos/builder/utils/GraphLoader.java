package io.intino.konos.builder.utils;

import com.intellij.openapi.diagnostic.Logger;
import io.intino.konos.model.KonosApplication;
import io.intino.tara.io.Stash;
import io.intino.tara.magritte.Graph;

public class GraphLoader {
	private static final Logger LOG = Logger.getInstance("GraphLoader");

	public static KonosApplication loadGraph(Stash... stash) {
		final ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(GraphLoader.class.getClassLoader());
		final Graph graph = Graph.use(KonosApplication.class, null).loadStashes(stash);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return graph.application();
	}

}
