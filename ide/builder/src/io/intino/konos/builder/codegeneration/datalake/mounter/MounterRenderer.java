package io.intino.konos.builder.codegeneration.datalake.mounter;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.MessageHandler;
import io.intino.konos.model.graph.Mounter;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static java.util.stream.Collectors.toList;

public class MounterRenderer {

	private final List<Mounter> mounters;
	private final File src;
	private final String packageName;
	private final String boxName;

	public MounterRenderer(KonosGraph graph, File src, String packageName, String boxName) {
		this.mounters = graph.nessClient(0).messageHandlerList().stream().filter(h -> h.i$(Mounter.class)).map(h -> h.a$(Mounter.class)).collect(toList());
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
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
			final File destination = new File(src, "ness/mounters");
			final String handlerName = Formatters.firstUpperCase(name) + "Mounter";
			if (!alreadyRendered(destination, handlerName)) Commons.writeFrame(destination, handlerName,
					Formatters.customize(MounterTemplate.create()).format(frame));
		}
	}

	private String composedName(MessageHandler handler) {
		return handler.subdomain().isEmpty() ? "" : handler.subdomain() + firstUpperCase(name(handler));
	}

	private String name(MessageHandler handler) {
		return handler.schema() == null ? handler.name$() : handler.schema().name$();
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}

}