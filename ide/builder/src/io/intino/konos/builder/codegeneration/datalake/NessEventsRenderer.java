package io.intino.konos.builder.codegeneration.datalake;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.DataLake;
import io.intino.konos.model.Konos;
import io.intino.tara.magritte.Graph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

public class NessEventsRenderer {
	private final DataLake dataLake;
	private final File gen;
	private final String packageName;
	private final String boxName;


	public NessEventsRenderer(Graph graph, File gen, String packageName, String boxName) {
		this.dataLake = graph.wrapper(Konos.class).dataLake();
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		if (dataLake == null) return;
		Frame frame = new Frame().addTypes("events").
				addSlot("package", packageName).
				addSlot("name", dataLake.name()).
				addSlot("box", boxName).
				addSlot("eventHandler", dataLake.eventHandlerList().stream().map(this::frameOf).toArray(Frame[]::new));
		if (!dataLake.eventHandlerList().isEmpty()) frame.addSlot("eventHandlerImport", packageName);
		Commons.writeFrame(gen, "NessEvents", template().format(frame));
	}

	private Frame frameOf(DataLake.EventHandler handler) {
		return new Frame().addTypes("eventHandler").
				addSlot("name", handler.name()).
				addSlot("messageType", customize(handler.name(), handler.topic())).
				addSlot("simpleMessageType", handler.topic());
	}

	private Frame customize(String name, String topic) {
		Frame frame = new Frame().addTypes("messageType");
		frame.addSlot("name", topic);
		for (String parameter : Commons.extractParameters(topic))
			frame.addSlot("custom", custom(name, parameter));
		return frame;
	}

	private Frame custom(String name, String parameter) {
		return new Frame().addSlot("value", parameter).addSlot("conf", name);
	}

	private Template template() {
		return Formatters.customize(NessEventsTemplate.create()).add("shortPath", value -> {
			String[] names = value.toString().split("\\.");
			return names[names.length - 1];
		});
	}

}
