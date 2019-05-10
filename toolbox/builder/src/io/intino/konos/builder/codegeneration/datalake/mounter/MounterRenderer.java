package io.intino.konos.builder.codegeneration.datalake.mounter;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Mounter;

import java.io.File;
import java.util.List;
import java.util.Map;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MounterRenderer {
	private final List<Mounter> mounters;
	private final File src;
	private final String packageName;
	private final String boxName;
	private final Map<String, String> classes;

	public MounterRenderer(KonosGraph graph, File src, String packageName, String boxName, Map<String, String> classes) {
		this.mounters = graph.nessClient(0).mounterList();
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
		this.classes = classes;
	}

	public void execute() {
		for (Mounter mounter : mounters) {
			final FrameBuilder builder = new FrameBuilder("mounter").
					add("box", boxName).
					add("package", packageName).
					add("name", mounter.name());
			if (mounter.schema() != null) {
				builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName));
				builder.add("type", new FrameBuilder("schema").add("package", packageName).add("name", mounter.schema().name$()));
			} else builder.add("type", "message");
			final File destination = new File(src, "datalake/mounters");
			final String handlerName = mounter.name() + "Mounter";
			classes.put(mounter.getClass().getSimpleName() + "#" + mounter.name$(), "datalake.mounters." + handlerName);
			if (!alreadyRendered(destination, handlerName))
				writeFrame(destination, handlerName, customize(new MounterTemplate()).render(builder.toFrame()));
		}
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}