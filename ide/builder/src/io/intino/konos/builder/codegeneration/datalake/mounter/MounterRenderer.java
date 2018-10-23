package io.intino.konos.builder.codegeneration.datalake.mounter;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Mounter;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;
import java.util.Map;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
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
			final String name = composedName(mounter);
			final Frame frame = new Frame().addTypes("mounter").
					addSlot("box", boxName).
					addSlot("package", packageName).
					addSlot("name", name);
			if (mounter.schema() != null) {
				frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
				frame.addSlot("type", new Frame("schema").addSlot("package", packageName).addSlot("name", mounter.schema().name$()));
			} else frame.addSlot("type", "message");
			final File destination = new File(src, "datalake/mounters");
			final String handlerName = name + "Mounter";
			classes.put(mounter.getClass().getSimpleName() + "#" + mounter.name$(), "datalake.mounters." + handlerName);
			if (!alreadyRendered(destination, handlerName))
				writeFrame(destination, handlerName, customize(MounterTemplate.create()).format(frame));
		}
	}

	private String composedName(Mounter mounter) {
		return firstUpperCase((mounter.subdomain().isEmpty() ? "" : Formatters.snakeCaseToCamelCase().format(mounter.subdomain().replace(".", "_"))) + firstUpperCase(mounter.name()));
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}