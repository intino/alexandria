package io.intino.konos.builder.codegeneration.datalake.feeder;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
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
import io.intino.konos.model.graph.usersensor.UserSensorSensor;
import io.intino.tara.magritte.Layer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class FeederRenderer extends Renderer {
	private final List<Feeder> feeders;
	private final NessClient nessClient;
	private final String domain;

	public FeederRenderer(Settings settings, KonosGraph graph) {
		super(settings, Target.Service);
		this.nessClient = graph.nessClient(0);
		this.domain = nessClient.domain();
		this.feeders = nessClient.feederList();
	}

	public static String name(Feeder feeder) {
		return isAnonymous(feeder) ? feeder.eventTypes().stream().map(s -> firstUpperCase(s.name$())).collect(Collectors.joining()) + "Feeder" : feeder.name$();
	}

	@Override
	public void render() {
		for (Feeder feeder : feeders) {
			final FrameBuilder builder = new FrameBuilder("feeder").
					add("box", boxName()).
					add("package", packageName()).
					add("name", name(feeder));
			for (Sensor sensor : feeder.sensorList())
				builder.add("sensor", frameOf(sensor, name(feeder)));
			builder.add("eventType", feeder.eventTypes().stream().filter(Objects::nonNull).map(s -> composedType(s, feeder.subdomain())).toArray(String[]::new));
			builder.add("domain", fullDomain(feeder.subdomain()));
			final String feederClassName = firstUpperCase(name(feeder));
			classes().put(feeder.getClass().getSimpleName() + "#" + name(feeder), "datalake.feeders." + feederClassName);
			writeFrame(new File(gen(), "datalake/feeders"), "Abstract" + feederClassName, customize(new AbstractFeederTemplate()).render(builder.toFrame()));
			if (!alreadyRendered(new File(src(), "datalake/feeders"), feederClassName))
				writeFrame(new File(src(), "datalake/feeders"), feederClassName, customize(new FeederTemplate()).render(builder.toFrame()));
		}
	}

	private Frame frameOf(Sensor sensor, String feeder) {
		FrameBuilder builder = new FrameBuilder("sensor").
				add("name", sensor.name$()).
				add("feeder", feeder).
				add("type", sensor.core$().conceptList().get(0).id().replaceAll("#.*", "") + "Sensor").
				add("parent", parent(sensor));

		if (sensor.isUserSensor()) {
			UserSensorSensor userSensor = sensor.asUserSensor();
			if (userSensor.width() != 100)
				builder.add("width", new FrameBuilder("width").add("value", userSensor.width()));
			if (userSensor.height() != 100)
				builder.add("height", new FrameBuilder("height").add("value", userSensor.height()));
		}
		return builder.toFrame();
	}

	private Frame parent(Sensor sensor) {
		if (sensor.isPoll()) return poll(sensor.asPoll());
		if (sensor.isFormEdition()) return formEdition(sensor.asFormEdition());
		if (sensor.isDocumentEdition()) return documentEdition(sensor.asDocumentEdition());
		if (sensor.isDocumentSignature()) return documentSignature(sensor.asDocumentSignature());
		return new FrameBuilder().toFrame();
	}

	private Frame poll(PollSensor sensor) {
		return new FrameBuilder("poll").
				add("defaultOption", sensor.defaultOption() == null ? "" : sensor.defaultOption()).
				add("eventMethod", sensor.core$().ownerAs(Feeder.class).eventTypes().stream().map(Layer::name$).toArray(String[]::new)).
				add("option", frameOf(sensor.optionList())).toFrame();
	}

	private Frame[] frameOf(List<PollSensor.Option> options) {
		List<Frame> frames = new ArrayList<>();
		for (PollSensor.Option option : options) {
			final FrameBuilder builder = new FrameBuilder("option").
					add("value", option.value()).
					add("event", option.event().name$());
			if (!option.optionList().isEmpty()) builder.add("option", frameOf(option.optionList()));
			frames.add(builder.toFrame());
		}
		return frames.toArray(new Frame[0]);
	}

	private Frame formEdition(FormEditionSensor sensor) {
		return new FrameBuilder("formEdition").add("path", sensor.path()).toFrame();
	}

	private Frame documentEdition(DocumentEditionSensor sensor) {
		return new FrameBuilder("documentEdition").add("mode", sensor.mode().name()).toFrame();
	}

	private Frame documentSignature(DocumentSignatureSensor sensor) {
		return new FrameBuilder("documentSignature").add("signType", sensor.signType().name()).add("signFormat", sensor.signFormat().name()).toFrame();
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

	private static boolean isAnonymous(Feeder feeder) {
		return feeder.name$().matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
	}

}
