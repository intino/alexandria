package io.intino.konos.builder.codegeneration.eventhandling;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Bus;
import io.intino.tara.magritte.Graph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class BusRenderer {
	private final List<Bus> bus;
	private final File gen;
	private final String packageName;
	private final String boxName;


	public BusRenderer(Graph graph, File gen, String packageName, String boxName) {
		bus = graph.find(Bus.class);
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		bus.forEach(this::processBus);
	}

	private void processBus(Bus bus) {
		Frame frame = new Frame().addTypes("bus").
				addSlot("package", packageName).
				addSlot("name", bus.name()).
				addSlot("box", boxName).
				addSlot("eventHandler", bus.eventHandlerList().stream().map(this::frameOf).toArray(Frame[]::new));
		Commons.writeFrame(gen, snakeCaseToCamelCase(bus.name()) + "Bus", template().format(frame));
	}

	private Frame frameOf(Bus.EventHandler handler) {
		final Frame frame = new Frame().addTypes("eventHandler").addSlot("name", handler.name()).addSlot("messageType", customize(handler.name(), handler.messageType()));
		if (handler.isDurable())
			frame.addSlot("durable", customizeDurable(handler.name(), handler.asDurable().messageType()));
		return frame;
	}

	private Frame customize(String name, String path) {
		Frame frame = new Frame().addTypes("messageType");
		frame.addSlot("name", path);
		for (String parameter : Commons.extractParameters(path))
			frame.addSlot("custom", custom(name, parameter));
		return frame;
	}

	private Frame customizeDurable(String name, String clientId) {
		Frame frame = new Frame().addTypes("durable").addSlot("value", "").addSlot("conf", name);
		for (String parameter : Commons.extractParameters(clientId)) frame.addSlot("custom", custom(name, parameter));
		return frame;
	}

	private Frame custom(String name, String parameter) {
		return new Frame().addSlot("value", parameter).addSlot("conf", name);
	}


	private Template template() {
		return Formatters.customize(BusTemplate.create());
	}

}
