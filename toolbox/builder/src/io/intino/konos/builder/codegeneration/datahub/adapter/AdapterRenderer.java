package io.intino.konos.builder.codegeneration.datahub.adapter;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Adapter;
import io.intino.konos.model.graph.KonosGraph;

import java.io.File;
import java.util.List;
import java.util.Map;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class AdapterRenderer {
	private final List<Adapter> adapters;
	private final File src;
	private final String packageName;
	private final String boxName;
	private final Map<String, String> classes;

	public AdapterRenderer(KonosGraph graph, File src, String packageName, String boxName, Map<String, String> classes) {
		this.adapters = graph.dataHub().adapterList();
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
		this.classes = classes;
	}

	public void execute() {
		for (Adapter adapter : adapters) {
			final FrameBuilder builder = new FrameBuilder("adapter").
					add("box", boxName).
					add("package", packageName).
					add("name", adapter.name$());
			final File directory = new File(src, "datahub/adapters");
			final String adapterName = adapter.name$();
			classes.put(adapter.getClass().getSimpleName() + "#" + adapter.name$(), "datahub.adapters." + adapterName);
			if (!alreadyRendered(directory, adapterName))
				writeFrame(directory, adapterName, customize(new AdapterTemplate()).render(builder.toFrame()));
		}
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}