package io.intino.konos.builder.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiFile;
import io.intino.konos.model.Konos;
import io.intino.tara.StashBuilder;
import io.intino.tara.io.Stash;
import io.intino.tara.magritte.Graph;

import java.io.File;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class GraphLoader {
	private static final Logger LOG = Logger.getInstance("GraphLoader");

	public static Konos loadGraph(Stash... stashes) {
		final ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(GraphLoader.class.getClassLoader());
		final Graph graph = Graph.use(Konos.class).load("Konos").loadStashes(stashes);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return graph.wrapper(Konos.class);
	}

	public static Graph loadGraph(Module module) {
		final List<PsiFile> konosFiles = KonosUtils.findKonosFiles(module);
		if (!konosFiles.isEmpty()) {
			final Stash stash = new StashBuilder(konosFiles.stream().map(pf ->
					new File(pf.getVirtualFile().getPath())).collect(toList()), new tara.dsl.Konos(), module.getName()).build();
			if (stash == null) {
				return null;
			} else return GraphLoader.loadGraph(stash).graph();
		} else return GraphLoader.loadGraph().graph();
	}

}
