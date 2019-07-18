package io.intino.konos.builder.codegeneration.datahub.feeder;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.DataHub.Tank;
import io.intino.konos.model.graph.Feeder;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Sensor;
import io.intino.konos.model.graph.documentedition.DocumentEditionSensor;
import io.intino.konos.model.graph.documentsignature.DocumentSignatureSensor;
import io.intino.konos.model.graph.formedition.FormEditionSensor;
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

public class FeederRenderer {
	private final Settings settings;
	private final List<Feeder> feeders;

	public FeederRenderer(Settings settings, KonosGraph graph) {
		this.settings = settings;
		this.feeders = graph.dataHub().feederList();
	}

	public static String name(Feeder feeder) {
		return isAnonymous(feeder) ? feeder.tanks().stream().map(s -> firstUpperCase(s.name$())).collect(Collectors.joining()) + "Feeder" : feeder.name$();
	}

	private static boolean isAnonymous(Feeder feeder) {
		return feeder.name$().matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
	}

	public void execute() {
		for (Feeder feeder : feeders) {
			final FrameBuilder builder = new FrameBuilder("feeder", feeder.sensorList().isEmpty() ? "simple" : "complex").
					add("box", settings.boxName()).
					add("package", settings.packageName()).
					add("name", name(feeder));
			for (Sensor sensor : feeder.sensorList())
				builder.add("sensor", frameOf(sensor, name(feeder)));
			builder.add("eventType", feeder.tanks().stream().filter(Objects::nonNull).map(Tank::fullName).toArray(String[]::new));
			final String feederClassName = firstUpperCase(name(feeder));
			settings.classes().put(feeder.getClass().getSimpleName() + "#" + name(feeder), "datahub.feeders." + feederClassName);
			writeFrame(new File(settings.gen(Target.Owner), "datahub/feeders"), "Abstract" + feederClassName, customize(new AbstractFeederTemplate()).render(builder.toFrame()));
			if (!alreadyRendered(new File(settings.src(Target.Owner), "datahub/feeders"), feederClassName))
				writeFrame(new File(settings.src(Target.Owner), "datahub/feeders"), feederClassName, customize(new FeederTemplate()).render(builder.toFrame()));
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
			if (userSensor.width() != 100) builder.add("width", new FrameBuilder("width").add("value", userSensor.width()));
			if (userSensor.height() != 100) builder.add("height", new FrameBuilder("height").add("value", userSensor.height()));
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
				add("eventMethod", sensor.core$().ownerAs(Feeder.class).tanks().stream().map(Layer::name$).toArray(String[]::new)).
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


	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}
