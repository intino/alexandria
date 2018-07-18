package io.intino.konos.builder.codegeneration.datalake.feeder;

import io.intino.konos.builder.codegeneration.datalake.mounter.MounterTemplate;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Feeder;
import io.intino.konos.model.graph.KonosGraph;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class FeederRenderer {

	private final List<Feeder> feeders;
	private final File src;
	private final String packageName;
	private final String boxName;
	private final Map<String, String> classes;

	public FeederRenderer(KonosGraph graph, File src, String packageName, String boxName, Map<String, String> classes) {
		this.feeders = graph.nessClient(0).feederList();
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
		this.classes = classes;
	}

	public void execute() {
		for (Feeder feeder : feeders) {
			final Frame frame = new Frame().addTypes("feeder").
					addSlot("box", boxName).
					addSlot("package", packageName).
					addSlot("name", feeder.name$());
//			for (Feeder.Sensor feed : feeder.sensorList())TODO
//				frame.addSlot("feed", new Frame("feed").addSlot("name", feed.name()));
			frame.addTypes(feeder.core$().conceptList().stream().map(c -> c.name()).collect(Collectors.toList()));
			final File destination = new File(src, "ness/feeders");
			final String feederClassName = feeder.name$() + "Feeder";
			classes.put(feeder.getClass().getSimpleName() + "#" + feeder.name$(), "ness.feeders." + feederClassName);
			if (!alreadyRendered(destination, feederClassName))
				writeFrame(destination, feederClassName, customize(MounterTemplate.create()).format(frame));
		}
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}
