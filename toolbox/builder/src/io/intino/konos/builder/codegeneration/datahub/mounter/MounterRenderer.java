package io.intino.konos.builder.codegeneration.datahub.mounter;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Mounter;
import io.intino.konos.model.graph.Schema;
import io.intino.konos.model.graph.population.PopulationMounter;

import java.io.File;
import java.util.List;
import java.util.Map;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static java.util.stream.Collectors.toList;

public class MounterRenderer {
	private final List<Mounter> mounters;
	private final File gen;
	private final File src;
	private final String packageName;
	private final String boxName;
	private final Map<String, String> classes;

	public MounterRenderer(KonosGraph graph, File gen, File src, String packageName, String boxName, Map<String, String> classes) {
		this.mounters = graph.dataHub().mounterList();
		this.gen = gen;
		this.src = src;
		this.packageName = packageName;
		this.boxName = boxName;
		this.classes = classes;
	}

	public void execute() {
		for (Mounter mounter : mounters) {
			final FrameBuilder builder = new FrameBuilder("mounter").
					add("box", boxName).
					add("package", packageName).
					add("name", mounter.name$());
			if (mounter.isPopulation()) {
				populationMounter(builder, mounter.asPopulation());
				classes.put(mounter.getClass().getSimpleName() + "#" + mounter.name$(), "datahub.mounters." + (mounter.name$() + "Mounter"));
				writeFrame(new File(gen, "datahub/mounters"), mounter.name$() + "Mounter", customize(new MounterTemplate()).render(builder.toFrame()));
				writeFrame(new File(src, "datahub/mounters"), mounter.name$() + "MounterFunctions", customize(new MounterTemplate()).render(builder.add("src").toFrame()));
			} else if (!mounter.isRealtime()) {
				writeFrame(new File(src, "datahub/mounters"), mounter.name$() + "Mounter", customize(new MounterTemplate()).render(builder.add("batch").toFrame()));
			} else {
				realtimeMounter(builder, mounter);
				final File destination = new File(src, "datahub/mounters");
				final String mounterName = mounter.name$() + "Mounter";
				classes.put(mounter.getClass().getSimpleName() + "#" + mounter.name$(), "datahub.mounters." + mounterName);
				if (!alreadyRendered(destination, mounterName))
					writeFrame(destination, mounterName, customize(new MounterTemplate()).render(builder.toFrame()));
			}
		}
	}

	private void populationMounter(FrameBuilder builder, PopulationMounter mounter) {
		List<PopulationMounter.Column> splittedColumns = mounter.columnList().stream().filter(c -> c.tank().split() != null).collect(toList());
		if (!splittedColumns.isEmpty()) builder.add("population").add("splittedColumns", splitted(splittedColumns));
		builder.add("column", mounter.columnList().stream().map(column -> builderOf(column).toFrame()).toArray(Frame[]::new));
		builder.add("datamart", mounter.collect().datamart().name$());
		builder.add("format", mounter.collect().format().name());
	}

	private Frame splitted(List<PopulationMounter.Column> splittedColumns) {
		return new FrameBuilder("splittedColumns").add("column", splittedColumns.stream().map(column -> builderOf(column).add("splitted").toFrame()).toArray(Frame[]::new)).toFrame();
	}

	private FrameBuilder builderOf(PopulationMounter.Column column) {
		FrameBuilder builder = new FrameBuilder("column")
				.add("fullName", column.tank().fullName())
				.add("name", column.tank().name$())
				.add("type", column.type().name())
				.add("mounter", column.core$().ownerAs(Mounter.class).name$());
		if (column.isId()) builder.add("facet", "id");
		return builder;
	}

	private void realtimeMounter(FrameBuilder builder, Mounter mounter) {
		builder.add("realtime");
		if (mounter.asRealtime().selectList().size() == 1) {
			Schema schema = mounter.asRealtime().select(0).tank().asEvent().schema();
			if (schema != null) {
				builder.add("schemaImport", new FrameBuilder("schemaImport").add("package", packageName));
				builder.add("type", new FrameBuilder("schema").add("package", packageName).add("name", schema.name$()));
			} else builder.add("type", "message");
		}
	}

	private boolean alreadyRendered(File destination, String action) {
		return Commons.javaFile(destination, action).exists();
	}
}