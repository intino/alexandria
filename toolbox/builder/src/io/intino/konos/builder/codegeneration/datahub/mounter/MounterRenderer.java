package io.intino.konos.builder.codegeneration.datahub.mounter;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Datamart;
import io.intino.konos.model.graph.Datamart.Mounter;
import io.intino.konos.model.graph.KonosGraph;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class MounterRenderer {
	private final CompilationContext context;
	private final KonosGraph graph;
	private final File genMounters;

	public MounterRenderer(CompilationContext context, KonosGraph graph) {
		this.context = context;
		this.graph = graph;
		this.genMounters = new File(context.gen(Target.Owner), "mounters");
	}

	public void execute() {
		CompilationContext.DataHubManifest manifest = context.dataHubManifest();
		for (Datamart datamart : graph.datamartList())
			for (Mounter mounter : datamart.mounterList()) {
				final String mounterName = mounter.name$();
				final FrameBuilder builder = baseFrame(mounter);
				if (mounter.isPopulation())
					populationMounter(mounter, mounterName, builder);
				else if (manifest != null && !alreadyRendered(context.src(Target.Owner), mounterName))
					eventMounter(mounter, mounterName, manifest, builder);
			}
	}

	private FrameBuilder baseFrame(Mounter mounter) {
		return new FrameBuilder("mounter").
				add("box", context.boxName()).
				add("package", context.packageName()).
				add("name", mounter.name$());
	}

	private void eventMounter(Mounter mounter, String mounterName, CompilationContext.DataHubManifest manifest, FrameBuilder builder) {
		String datamart = mounter.core$().ownerAs(Datamart.class).name$();
		builder.add("event").add("datamart", datamart).add("type", types(mounter, manifest));
		context.classes().put(mounter.getClass().getSimpleName() + "#" + mounter.name$(), "mounters." + datamart + "." + mounterName);
		File datamartFolder = new File(context.src(Target.Owner), datamart);
		File mounters = new File(datamartFolder, "mounters");
		if (!alreadyRendered(mounters, mounterName)) {
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(mounter), javaFile(mounters, firstUpperCase(mounterName)).getAbsolutePath()));
			writeFrame(mounters, mounterName, customize(new MounterTemplate()).render(builder.toFrame()));
		}
	}

	private String[] types(Mounter mounter, CompilationContext.DataHubManifest manifest) {
		return mounter.asEvent().requireList().stream().map(r -> manifest.tankClasses.get(r.tank())).filter(Objects::nonNull).toArray(String[]::new);
	}

	private void populationMounter(Mounter mounter, String mounterName, FrameBuilder builder) {
		populationMounter(builder, mounter.asPopulation());
		context.classes().put(mounter.getClass().getSimpleName() + "#" + mounter.name$(), "mounters." + mounterName);
		writeFrame(genMounters, mounter.name$(), customize(new MounterTemplate()).render(builder.toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(mounter), javaFile(genMounters, mounter.name$()).getAbsolutePath()));
		mounterFunctions(mounter);
	}

	private void mounterFunctions(Mounter mounter) {
		String datamart = mounter.core$().ownerAs(Datamart.class).name$();
		mounter.asPopulation().clear().split(t -> true);
		FrameBuilder baseFrame = baseFrame(mounter).add("src");
		populationMounter(baseFrame, mounter.asPopulation());
		File datamartFolder = new File(context.src(Target.Owner), datamart);
		File mounters = new File(datamartFolder, "mounters");
		if (!alreadyRendered(mounters, mounter.name$() + "MounterFunctions"))
			writeFrame(mounters, mounter.name$() + "MounterFunctions", customize(new MounterTemplate()).render(baseFrame.toFrame()));
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