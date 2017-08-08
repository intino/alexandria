package io.intino.konos.builder.codegeneration.datalake;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.DataLake;
import io.intino.konos.model.graph.KonosGraph;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

public class NessTanksRenderer {
	private final DataLake dataLake;
	private final File gen;
	private final String packageName;
	private final String boxName;


	public NessTanksRenderer(KonosGraph graph, File gen, String packageName, String boxName) {
		this.dataLake = graph.dataLake();
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
	}

	public void execute() {
		if (dataLake == null) return;
		Frame frame = new Frame().addTypes("tanks").
				addSlot("package", packageName).
				addSlot("name", dataLake.name$()).
				addSlot("box", boxName).
				addSlot("tank", dataLake.tankList().stream().map(this::frameOf).toArray(Frame[]::new));
		if (!dataLake.tankList().isEmpty()) frame.addSlot("tankImport", packageName);
		Commons.writeFrame(gen, "NessTanks", template().format(frame));
	}

	private Frame frameOf(DataLake.Tank handler) {
		return new Frame().addTypes("tank").
				addSlot("name", handler.name$()).
				addSlot("box", boxName).
				addSlot("messageType", customize(handler.name$(), handler.topic())).
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
		return Formatters.customize(NessTanksTemplate.create()).add("shortPath", value -> {
			String[] names = value.toString().split("\\.");
			return names[names.length - 1];
		});
	}

}
