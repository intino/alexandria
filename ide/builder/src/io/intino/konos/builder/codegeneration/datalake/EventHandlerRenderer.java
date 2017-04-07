package io.intino.konos.builder.codegeneration.datalake;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.DataLake.EventHandler;
import io.intino.tara.magritte.Graph;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

public class EventHandlerRenderer {

	private final List<EventHandler> eventHandlers;
	private final File src;
	private final String packageName;
	private final String boxName;

	public EventHandlerRenderer(Graph graph, File src, String packageName, String boxName) {
		this.eventHandlers = graph.find(EventHandler.class);
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		for (EventHandler eventHandler : eventHandlers) {
			final Frame frame = new Frame().addTypes("eventHandler").
					addSlot("box", boxName).
					addSlot("package", packageName).
					addSlot("name", eventHandler.name());
			final File destination = new File(src, "eventhandlers");
			final String name = Formatters.firstUpperCase(eventHandler.name()) + "EventHandler";
			if (!alreadyRendered(destination, name)) Commons.writeFrame(destination, name,
					Formatters.customize(EventHandlerTemplate.create()).format(frame));
		}
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}


}
