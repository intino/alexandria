package io.intino.konos.builder.utils;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.StashBuilder;
import io.intino.tara.io.Stash;
import io.intino.tara.magritte.Graph;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphLoader {

	public static KonosGraph loadGraph(Module module) {
		final List<PsiFile> konosFiles = KonosUtils.findKonosFiles(module);
		if (!konosFiles.isEmpty()) {
			final Map<File, Charset> files = konosFiles.stream().map(PsiFile::getVirtualFile).collect(Collectors.toMap(vf -> new File(vf.getPath()), VirtualFile::getCharset));
			final Stash stash = new StashBuilder(files, new tara.dsl.Konos(), module.getName()).build();
			if (stash == null) return null;
			else return loadGraph(stash);
		} else return loadGraph();
	}

	public static KonosGraph loadGraph(Stash... stashes) {
		final ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(GraphLoader.class.getClassLoader());
		final Graph graph = new Graph().loadStashes("Konos").loadStashes(stashes);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return graph == null ? null : graph.as(KonosGraph.class);
	}

}
