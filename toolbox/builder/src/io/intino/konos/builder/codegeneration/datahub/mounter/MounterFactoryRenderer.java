package io.intino.konos.builder.codegeneration.datahub.mounter;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Mounter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MounterFactoryRenderer {
	private final Settings settings;
	private final List<Mounter> mounters;
	private File genMounters;

	public MounterFactoryRenderer(Settings settings, KonosGraph graph) {
		this.settings = settings;
		this.mounters = graph.mounterList().stream().filter(Mounter::isRealTime).collect(Collectors.toList());
		this.genMounters = new File(settings.gen(Target.Owner), "mounters");
	}

	public void execute() {
		if (mounters.isEmpty()) return;
		FrameBuilder builder = baseFrame("factory");
		for (Mounter mounter : mounters) builder.add("mounter", baseFrame("mounter").add("name", mounter.name$()));
		settings.classes().put(Mounter.class.getSimpleName() + "#" + "MounterFactory", "mounters.mounterFactory");
		Frame object = builder.toFrame();
		writeFrame(genMounters, "MounterFactory", customize(new MounterFactoryTemplate()).render(object));
		writeFrame(genMounters, "Mounter", customize(new IMounterTemplate()).render(object));
	}

	@NotNull
	private FrameBuilder baseFrame(String name) {
		return new FrameBuilder(name).
				add("box", settings.boxName()).
				add("package", settings.packageName());
	}
}