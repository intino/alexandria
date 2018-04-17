package io.intino.konos.builder.codegeneration.datalake;

import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.DataLake;
import io.intino.konos.model.graph.KonosGraph;
import org.jetbrains.annotations.NotNull;
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
		Frame frame = new Frame().addTypes("tanks").
				addSlot("package", packageName).
				addSlot("name", dataLake.name$()).
				addSlot("box", boxName).
				addSlot("tank", dataLake.tankList().stream().map(this::frameOf).toArray(Frame[]::new));
		if (!dataLake.tankList().isEmpty()) frame.addSlot("tankImport", packageName);
		Commons.writeFrame(new File(gen, "ness"), "NessTanks", template().format(frame));
	}

	private Frame frameOf(DataLake.Tank tank) {
		final String type = type(tank);
		return new Frame().addTypes("tank").
				addSlot("name", type).
				addSlot("box", boxName).
				addSlot("messageType", dataLake.domain() + "." + subdomain(tank) + type).
				addSlot("simpleMessageType", type);
	}

	@NotNull
	private String subdomain(DataLake.Tank tank) {
		return tank.subdomain().isEmpty() ? "" : tank.subdomain() + ".";
	}

	private String type(DataLake.Tank tank) {
		return tank.schema() == null ? tank.name$() : tank.schema().name$();
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
