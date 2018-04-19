package io.intino.konos.builder.codegeneration.datalake;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.DataLake.Tank;
import io.intino.konos.model.graph.KonosGraph;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class MessageHandlerRenderer {

	private final List<Tank> tanks;
	private final File src;
	private final String packageName;
	private final String boxName;

	public MessageHandlerRenderer(KonosGraph graph, File src, String packageName, String boxName) {
		this.tanks = graph.dataLake() != null ? graph.dataLake().tankList() : Collections.emptyList();
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		for (Tank tank : tanks) {
			final String name = tank.schema() != null ? tank.schema().name$() : tank.name$();
			final Frame frame = new Frame().addTypes("messageHandler").
					addSlot("box", boxName).
					addSlot("package", packageName).
					addSlot("name", name);
			if (tank.schema() != null) {
				frame.addSlot("schemaImport", new Frame().addTypes("schemaImport").addSlot("package", packageName));
				frame.addSlot("type", new Frame("schema").addSlot("package", packageName).addSlot("name", tank.schema().name$()));
			} else frame.addSlot("type", "message");
			final File destination = new File(src, "ness/messagehandlers");
			final String handlerName = Formatters.firstUpperCase(name) + "MessageHandler";
			if (!alreadyRendered(destination, handlerName)) Commons.writeFrame(destination, handlerName,
					Formatters.customize(MessageHandlerTemplate.create()).format(frame));
		}
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}