package io.intino.konos.builder.codegeneration.datahub.mounter;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Datamart;
import io.intino.konos.model.graph.Datamart.Mounter;
import io.intino.konos.model.graph.KonosGraph;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MounterRenderer {
	private final Settings settings;
	private final File sourceMounters;
	private final KonosGraph graph;
	private final File src;
	private final File gen;
	private File genMounters;

	public MounterRenderer(Settings settings, KonosGraph graph) {
		this.settings = settings;
		this.graph = graph;
		src = settings.src(Target.Owner);
		this.sourceMounters = new File(src, "mounters");
		gen = settings.gen(Target.Owner);
		this.genMounters = new File(gen, "mounters");
	}

	public void execute() {
		Settings.DataHubManifest manifest = settings.dataHubManifest();
		for (Datamart datamart : graph.datamartList())
			for (Mounter mounter : datamart.mounterList()) {
				final String mounterName = mounter.name$();
				final FrameBuilder builder = baseFrame(mounter);
				if (mounter.isPopulation()) populationMounter(mounter, mounterName, builder);
				else if (manifest != null) eventMounter(mounter, mounterName, manifest, builder);
			}
	}

	@NotNull
	private FrameBuilder baseFrame(Mounter mounter) {
		return new FrameBuilder("mounter").
				add("box", settings.boxName()).
				add("package", settings.packageName()).
				add("name", mounter.name$());
	}

	private void eventMounter(Mounter mounter, String mounterName, Settings.DataHubManifest manifest, FrameBuilder builder) {
		String datamart = mounter.core$().ownerAs(Datamart.class).name$();
		builder.add("event").add("datamart", datamart).add("type", types(mounter, manifest));
		settings.classes().put(mounter.getClass().getSimpleName() + "#" + mounter.name$(), "mounters." + datamart + "." + mounterName);
		File destination = new File(src, datamart.toLowerCase() + File.separator + "mounters");
		if (!alreadyRendered(destination, mounterName)) {
			writeFrame(destination, mounterName, customize(new MounterTemplate()).render(builder.toFrame()));
		}
	}

	private String[] types(Mounter mounter, Settings.DataHubManifest manifest) {
		return mounter.asEvent().requireList().stream().map(r -> manifest.tankClasses.get(r.tank())).filter(Objects::nonNull).toArray(String[]::new);
	}

	private void populationMounter(Mounter mounter, String mounterName, FrameBuilder builder) {
		populationMounter(builder, mounter.asPopulation());
		settings.classes().put(mounter.getClass().getSimpleName() + "#" + mounter.name$(), "mounters." + mounterName);
		writeFrame(genMounters, mounter.name$(), customize(new MounterTemplate()).render(builder.toFrame()));
		mounterFunctions(mounter);
	}

	private void mounterFunctions(Mounter mounter) {
		mounter.asPopulation().clear().split(t -> true);
		FrameBuilder baseFrame = baseFrame(mounter).add("src");
		populationMounter(baseFrame, mounter.asPopulation());
		if (!alreadyRendered(sourceMounters, mounter.name$() + "MounterFunctions"))
			writeFrame(sourceMounters, mounter.name$() + "MounterFunctions", customize(new MounterTemplate()).render(baseFrame.toFrame()));
	}


	private Object name(String tank) {
		return tank.replace(".", " ");
	}

	private void populationMounter(FrameBuilder builder, Mounter.Population mounter) {
		List<FrameBuilder> frames = new ArrayList<>();
		mounter.sourceList().stream().map(this::buildersOf).forEach(frames::addAll);
		builder.add("population").
				add("column", frames.toArray(new FrameBuilder[0])).
				add("datamart", mounter.core$().ownerAs(Datamart.class).name$()).
				add("format", mounter.format().name());
	}

	private List<FrameBuilder> buildersOf(Mounter.Population.Source source) {
		List<FrameBuilder> builders = new ArrayList<>();
		for (String tank : tanksOf(source.core$().ownerAs(Mounter.Population.class), source.tank())) {
			FrameBuilder builder = new FrameBuilder("column")
					.add("fullName", tank)
					.add("name", source.tank())
					.add("type", source.type().name())
					.add("mounter", source.core$().ownerAs(Mounter.class).name$());
			if (source.isId()) builder.add("facet", "id");
			builders.add(builder);
		}
		return builders;
	}

	private List<String> tanksOf(Mounter.Population mounter, String tank) {
		List<String> tanks = new ArrayList<>();
		if (!mounter.splitList().isEmpty()) for (Mounter.Population.Split split : mounter.splitList())
			split.splits().stream().map(s -> s + "." + tank).forEach(tanks::add);
		else return Collections.singletonList(tank);
		return tanks;
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}