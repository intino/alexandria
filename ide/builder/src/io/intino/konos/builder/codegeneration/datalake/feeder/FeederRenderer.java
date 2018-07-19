package io.intino.konos.builder.codegeneration.datalake.feeder;

import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Feeder;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Schema;
import io.intino.konos.model.graph.Sensor;
import io.intino.konos.model.graph.documentedition.DocumentEditionSensor;
import io.intino.konos.model.graph.documentsignature.DocumentSignatureSensor;
import io.intino.konos.model.graph.formedition.FormEditionSensor;
import io.intino.konos.model.graph.ness.NessClient;
import io.intino.konos.model.graph.poll.PollSensor;
import io.intino.tara.magritte.Layer;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class FeederRenderer {

	private final List<Feeder> feeders;
	private final File src;
	private final String packageName;
	private final String boxName;
	private final Map<String, String> classes;
	private final NessClient nessClient;
	private final File gen;
	private final String domain;

	public FeederRenderer(KonosGraph graph, File gen, File src, String packageName, String boxName, Map<String, String> classes) {
		this.nessClient = graph.nessClient(0);
		this.gen = gen;
		this.domain = nessClient.domain();
		this.feeders = nessClient.feederList();
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
			for (Sensor sensor : feeder.sensorList())
				frame.addSlot("sensor", frameOf(sensor, feeder.name$()));
			frame.addSlot("eventType", feeder.eventTypes().stream().map(s -> composedType(s, feeder.subdomain())).toArray(String[]::new));
			frame.addSlot("domain", fullDomain(feeder.subdomain()));
			final String feederClassName = firstUpperCase(feeder.name$()) + "Feeder";
			classes.put(feeder.getClass().getSimpleName() + "#" + feeder.name$(), "ness.feeders." + feederClassName);
			writeFrame(new File(gen, "ness/feeders"), "Abstract" + feederClassName, customize(AbstractFeederTemplate.create()).format(frame));
			if (!alreadyRendered(new File(src, "ness/feeders"), feederClassName))
				writeFrame(new File(src, "ness/feeders"), feederClassName, customize(FeederTemplate.create()).format(frame));
		}
	}

	private Frame frameOf(Sensor sensor, String feeder) {
		return new Frame("sensor").
				addSlot("name", sensor.name$()).
				addSlot("feeder", feeder).
				addSlot("type", sensor.core$().conceptList().stream().map(c -> c.id().replaceAll("#.*", "")).toArray(String[]::new)).
				addSlot("parent", parent(sensor));
	}

	private Frame parent(Sensor sensor) {
		if (sensor.isPoll()) return poll(sensor.asPoll());
		if (sensor.isFormEdition()) return formEdition(sensor.asFormEdition());
		if (sensor.isDocumentEdition()) return documentEdition(sensor.asDocumentEdition());
		if (sensor.isDocumentSignature()) return documentSignature(sensor.asDocumentSignature());
		return new Frame();
	}

	private Frame poll(PollSensor sensor) {
		return new Frame("poll").
				addSlot("defaultOption", sensor.defaultOption()).
				addSlot("eventMethod", sensor.core$().ownerAs(Feeder.class).eventTypes().stream().map(Layer::name$).toArray(String[]::new)).
				addSlot("option", frameOf(sensor.optionList()));
	}

	private Frame[] frameOf(List<PollSensor.Option> options) {
		List<Frame> frames = new ArrayList<>();
		for (PollSensor.Option option : options) {
			final Frame frame = new Frame("option").
					addSlot("value", option.value()).
					addSlot("event", option.event().name$());

			if (!option.optionList().isEmpty()) frame.addSlot("option", frameOf(option.optionList()));
			frames.add(frame);
		}
		return frames.toArray(new Frame[0]);
	}

	private Frame formEdition(FormEditionSensor sensor) {
		return new Frame("formEdition").addSlot("path", sensor.path());
	}

	private Frame documentEdition(DocumentEditionSensor sensor) {
		return new Frame("documentEdition").addSlot("mode", sensor.mode().name());
	}

	private Frame documentSignature(DocumentSignatureSensor sensor) {
		return new Frame("documentSignature").addSlot("signType", sensor.signType().name()).addSlot("signFormat", sensor.signFormat().name());
	}

	private String composedType(Schema schema, String subdomain) {
		return fullDomain(subdomain) + schema.name$().toLowerCase();
	}

	private String fullDomain(String subdomain) {
		return (domain.isEmpty() ? "" : domain + ".") + (subdomain.isEmpty() ? "" : subdomain + ".");
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}
