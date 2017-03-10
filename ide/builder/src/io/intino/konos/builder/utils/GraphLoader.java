package io.intino.konos.builder.utils;

import com.intellij.openapi.diagnostic.Logger;
import io.intino.konos.model.Konos;
import io.intino.tara.io.Stash;
import io.intino.tara.magritte.Graph;

public class GraphLoader {
	private static final Logger LOG = Logger.getInstance("GraphLoader");

	public static Konos loadGraph(Stash... stashes) {
		final ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(GraphLoader.class.getClassLoader());
		final Graph graph = Graph.use(Konos.class).load("Konos").loadStashes(stashes);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return graph.wrapper(Konos.class);
	}

}
