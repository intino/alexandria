package io.intino.konos.builder.codegeneration.datalake;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.DataLake.Tank;
import io.intino.tara.magritte.Graph;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

public class MessageHandlerRenderer {

	private final List<Tank> eventHandlers;
	private final File src;
	private final String packageName;
	private final String boxName;

	public MessageHandlerRenderer(Graph graph, File src, String packageName, String boxName) {
		this.eventHandlers = graph.find(Tank.class);
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		for (Tank eventHandler : eventHandlers) {
			final Frame frame = new Frame().addTypes("messageHandler").
					addSlot("box", boxName).
					addSlot("package", packageName).
					addSlot("name", eventHandler.name());
			final File destination = new File(src, "messagehandlers");
			final String name = Formatters.firstUpperCase(eventHandler.name()) + "MessageHandler";
			if (!alreadyRendered(destination, name)) Commons.writeFrame(destination, name,
					Formatters.customize(MessageHandlerTemplate.create()).format(frame));
		}
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}


}
