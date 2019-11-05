package io.intino.konos.builder.codegeneration.feeder;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Feeder;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Sensor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class FeederRenderer {
	private final Settings settings;
	private final List<Feeder> feeders;

	public FeederRenderer(Settings settings, KonosGraph graph) {
		this.settings = settings;
		this.feeders = graph.feederList();
	}

	public void execute() {
		for (Feeder feeder : feeders) {
			final FrameBuilder builder = new FrameBuilder("feeder", feeder.sensorList().isEmpty() ? "simple" : "complex").
					add("box", settings.boxName()).
					add("package", settings.packageName()).
					add("name", feeder.name$());
			for (Sensor sensor : feeder.sensorList())
				builder.add("sensor", frameOf(sensor, feeder.name$()));
			builder.add("eventType", feeder.tanks().toArray(new String[0]));
			final String feederClassName = firstUpperCase(feeder.name$());
			settings.classes().put(feeder.getClass().getSimpleName() + "#" + feeder.name$(), "feeders." + feederClassName);
			writeFrame(new File(settings.gen(Target.Owner), "feeders"), "Abstract" + feederClassName, customize(new AbstractFeederTemplate()).render(builder.toFrame()));
			if (!alreadyRendered(new File(settings.src(Target.Owner), "feeders"), feederClassName))
				writeFrame(new File(settings.src(Target.Owner), "feeders"), feederClassName, customize(new FeederTemplate()).render(builder.toFrame()));
		}
	}

	private Frame frameOf(Sensor sensor, String feeder) {
		FrameBuilder builder = new FrameBuilder("sensor").
				add("name", sensor.name$()).
				add("feeder", feeder).
				add("type", sensor.core$().conceptList().get(0).id().replaceAll("#.*", "") + "Sensor").
				add("parent", parent(sensor));

		if (sensor.isUserSensor()) {
			Sensor.UserSensor userSensor = sensor.asUserSensor();
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

	private Frame poll(Sensor.Poll sensor) {
		return new FrameBuilder("poll").
				add("defaultOption", sensor.defaultOption() == null ? "" : sensor.defaultOption()).
				add("eventMethod", sensor.core$().ownerAs(Feeder.class).tanks().toArray(new String[0])).
				add("option", frameOf(sensor.optionList())).toFrame();
	}

	private Frame[] frameOf(List<Sensor.Poll.Option> options) {
		List<Frame> frames = new ArrayList<>();
		for (Sensor.Poll.Option option : options) {
			final FrameBuilder builder = new FrameBuilder("option").
					add("value", option.value()).
					add("event", option.event().name$());
			if (!option.optionList().isEmpty()) builder.add("option", frameOf(option.optionList()));
			frames.add(builder.toFrame());
		}
		return frames.toArray(new Frame[0]);
	}

	private Frame formEdition(Sensor.FormEdition sensor) {
		return new FrameBuilder("formEdition").add("path", sensor.path()).toFrame();
	}

	private Frame documentEdition(Sensor.DocumentEdition sensor) {
		return new FrameBuilder("documentEdition").add("mode", sensor.mode().name()).toFrame();
	}

	private Frame documentSignature(Sensor.DocumentSignature sensor) {
		return new FrameBuilder("documentSignature").add("signType", sensor.signType().name()).add("signFormat", sensor.signFormat().name()).toFrame();
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}
