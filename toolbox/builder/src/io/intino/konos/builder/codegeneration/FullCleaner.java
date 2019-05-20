package io.intino.konos.builder.codegeneration;

import io.intino.konos.builder.codegeneration.services.ui.ServiceListCleaner;
import io.intino.konos.model.graph.KonosGraph;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FullCleaner extends Cleaner {
	private final KonosGraph graph;
	private static final List<String> ExcludedDirectories = Arrays.asList("displays", ".cache");

	public FullCleaner(Settings settings, KonosGraph graph) {
		super(settings);
		this.graph = graph;
	}

	@Override
	public void execute() {
		clean(gen());
		new ServiceListCleaner(settings, graph).execute();
	}

	private void clean(File directory) {
		if (!directory.exists()) return;
		List<File> files = Arrays.asList(directory.listFiles(pathname -> !ExcludedDirectories.contains(pathname.getName())));
		files.forEach(f -> {
			if (f.isDirectory()) clean(f);
			f.delete();
		});
	}

}
