package io.intino.pandora.builder.codegeneration.server.jms.connector;

import io.intino.pandora.builder.codegeneration.Formatters;
import io.intino.pandora.model.Bus;
import io.intino.pandora.builder.helpers.Commons;
import io.intino.tara.magritte.Graph;
import io.intino.tara.magritte.Layer;
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
				addSlot("channel", bus.channelList().stream().map(Layer::name).toArray(String[]::new));
		Commons.writeFrame(new File(gen, "bus"), snakeCaseToCamelCase(bus.name()) + "Bus", template().format(frame));
	}


	private Template template() {
		return Formatters.customize(BusTemplate.create());
	}

}
