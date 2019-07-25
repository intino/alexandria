package io.intino.konos.builder.codegeneration.datalake;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Datalake;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.batch.BatchMounter;
import io.intino.konos.model.graph.population.PopulationMounter;
import io.intino.konos.model.graph.realtime.RealtimeMounter;
import io.intino.konos.model.graph.realtime.RealtimeMounter.Source;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class DatalakeRenderer {
	private final Settings settings;
	private final KonosGraph graph;
	private final File genDirectory;

	public DatalakeRenderer(Settings settings, KonosGraph graph) {
		this.graph = graph;
		this.settings = settings;
		this.genDirectory = settings.gen(Target.Owner);
	}

	public void execute() {
		Datalake datalake = graph.datalake();
		if (datalake == null || !(datalake.isNfsMirrored() || datalake.isSshMirrored())) return;
		final FrameBuilder builder = new FrameBuilder("datalake", datalake.isNfsMirrored() ? "nfs" : "ssh").add("package", settings.packageName());
		List<String> eventTanks = new ArrayList<>();
		graph.realtimeMounterList().forEach(mounter -> eventTanks.addAll(tanksOf(mounter)));
		graph.batchMounterList().forEach(mounter -> eventTanks.addAll(tanksOf(mounter)));
		List<String> setTanks = new ArrayList<>();
		builder.add("eventTank", eventTanks.toArray(new String[0]));
		graph.populationMounterList().forEach(mounter -> setTanks.addAll(tanksOf(mounter)));
		builder.add("setTank", setTanks.toArray(new String[0]));
		settings.classes().put("Datalake", "Datalake");
		File destination = genDirectory;
		if (!Commons.javaFile(destination, "Datalake").exists())
			writeFrame(destination, "Datalake", customize(new DatalakeTemplate()).render(builder.toFrame()));
	}

	private List<String> tanksOf(BatchMounter mounter) {
		return mounter.sourceList().stream().map(BatchMounter.Source::tank).collect(Collectors.toList());
	}

	private List<String> tanksOf(RealtimeMounter mounter) {
		return mounter.sourceList().stream().map(Source::channel).collect(Collectors.toList());
	}

	private List<String> tanksOf(PopulationMounter mounter) {
		List<String> tanks = new ArrayList<>();
		if (!mounter.splitList().isEmpty()) for (PopulationMounter.Split split : mounter.splitList())
			split.splits().forEach(s -> mounter.sourceList().stream().map(source -> s + "." + source.tank()).forEach(tanks::add));
		else return mounter.sourceList().stream().map(PopulationMounter.Source::tank).collect(Collectors.toList());
		return tanks;
	}
}
