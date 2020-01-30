package io.intino.konos.builder.codegeneration.datahub.mounter;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.model.graph.Datamart;
import io.intino.konos.model.graph.Datamart.Mounter;
import io.intino.konos.model.graph.KonosGraph;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MounterFactoryRenderer {
	private final Settings settings;
	private final List<Mounter> mounters;
	private File genMounters;

	public MounterFactoryRenderer(Settings settings, KonosGraph graph) {
		this.settings = settings;
		this.mounters = graph.datamartList().stream().map(Datamart::mounterList).flatMap(Collection::stream).collect(Collectors.toList());
		this.genMounters = new File(settings.gen(Target.Owner), "mounters");
	}

	public void execute() {
		if (mounters.isEmpty()) return;
		Settings.DataHubManifest manifest = settings.dataHubManifest();
		if (manifest == null) return;

		FrameBuilder builder = baseFrame("factory");
		Map<String, List<Mounter>> map = map(mounters, manifest);

		for (String event : map.keySet())
			builder.add("event", baseFrame("event").
					add("name", event).
					add("mounter", map.get(event).stream().map(m -> baseFrame("mounter").add("datamart", m.core$().owner().name()).add("name", m.name$()).toFrame()).toArray(Frame[]::new)));
		settings.classes().put(Mounter.class.getSimpleName() + "#" + "MounterFactory", "mounters.mounterFactory");
		Frame object = builder.toFrame();
		writeFrame(genMounters, "MounterFactory", customize(new MounterFactoryTemplate()).render(object));
		writeFrame(genMounters, "Mounter", customize(new IMounterTemplate()).render(object));
	}

	private Map<String, List<Mounter>> map(List<Mounter> mounters, Settings.DataHubManifest manifest) {
		Map<String, List<Mounter>> mountersByRequire = new HashMap<>();
		for (Mounter mounter : mounters)
			mounter.asEvent().requireList().forEach(r -> {
				String key = manifest.tankClasses.get(r.tank());
				if (!mountersByRequire.containsKey(key)) mountersByRequire.put(key, new ArrayList<>());
				mountersByRequire.get(key).add(mounter);
			});
		return mountersByRequire;
	}

	@NotNull
	private FrameBuilder baseFrame(String name) {
		return new FrameBuilder(name).
				add("box", settings.boxName()).
				add("package", settings.packageName());
	}
}