package io.intino.konos.builder.codegeneration.datahub.mounter;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Mounter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MounterRenderer {
	private final Settings settings;
	private final List<Mounter> mounters;
	private final File sourceMounters;
	private File genMounters;

	public MounterRenderer(Settings settings, KonosGraph graph) {
		this.settings = settings;
		this.mounters = graph.mounterList();
		this.sourceMounters = new File(settings.src(Target.Owner), "mounters");
		this.genMounters = new File(settings.gen(Target.Owner), "mounters");
	}

	public void execute() {
		for (Mounter mounter : mounters) {
			final String mounterName = mounter.name$();
			final FrameBuilder builder = baseFrame(mounter);
			if (mounter.isPopulation()) populationMounter(mounter, mounterName, builder);
			else if (mounter.isBatch() && !alreadyRendered(sourceMounters, mounterName))
				batchMounter(mounter.asBatch(), mounterName, builder);
			else if (!alreadyRendered(sourceMounters, mounterName))
				mounter(mounter, mounterName, builder);
		}
	}

	@NotNull
	private FrameBuilder baseFrame(Mounter mounter) {
		return new FrameBuilder("mounter").
				add("box", settings.boxName()).
				add("package", settings.packageName()).
				add("name", mounter.name$());
	}

	private void mounter(Mounter mounter, String mounterName, FrameBuilder builder) {
		settings.classes().put(mounter.getClass().getSimpleName() + "#" + mounter.name$(), "mounters." + mounterName);
		writeFrame(sourceMounters, mounterName, customize(new MounterTemplate()).render(builder.toFrame()));
	}

	private void batchMounter(Mounter.Batch mounter, String mounterName, FrameBuilder builder) {
		builder.add("batch");
		mounter.sourceList().forEach(source -> builder.add("tank", new FrameBuilder("tank").add("qn", source.tank()).add("name", name(source.tank()))));
		writeFrame(new File(settings.src(Target.Owner), "mounters"), mounterName, customize(new MounterTemplate()).render(builder));
		settings.classes().put(mounter.getClass().getSimpleName() + "#" + mounter.name$(), "mounters." + mounterName);
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
				add("datamart", mounter.datamart().name$()).
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