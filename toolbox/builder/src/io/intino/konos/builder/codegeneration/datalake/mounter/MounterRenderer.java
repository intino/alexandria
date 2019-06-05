package io.intino.konos.builder.codegeneration.datalake.mounter;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Mounter;
import io.intino.konos.model.graph.Schema;
import io.intino.konos.model.graph.events.EventsSource;
import io.intino.konos.model.graph.population.PopulationSource;

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
		this.mounters = graph.mounterList();
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
			if (mounter.isBatch()) {
				batchMounter(builder, mounter);
				final File destination = new File(gen, "datalake/mounters");
				classes.put(mounter.getClass().getSimpleName() + "#" + mounter.name$(), "datalake.mounters." + (mounter.name$() + "Mounter"));
				writeFrame(destination, mounter.name$() + "Mounter", customize(new MounterTemplate()).render(builder.toFrame()));
			} else {
				realtimeMounter(builder, mounter);
				final File destination = new File(src, "datalake/mounters");
				final String mounterName = mounter.name$() + "Mounter";
				classes.put(mounter.getClass().getSimpleName() + "#" + mounter.name$(), "datalake.mounters." + mounterName);
				if (!alreadyRendered(destination, mounterName))
					writeFrame(destination, mounterName, customize(new MounterTemplate()).render(builder.toFrame()));
			}
		}
	}

	private void batchMounter(FrameBuilder builder, Mounter mounter) {
		builder.add("batch").add(mounter.source().core$().facetList().get(0));
		if (mounter.source().isPopulation()) {
			PopulationSource population = mounter.source().asPopulation();
			List<PopulationSource.Column> splittedColumns = population.columnList().stream().filter(c -> c.tank().split() != null).collect(toList());
			if (!splittedColumns.isEmpty()) {
				builder.add("splittedColumns", splitted(splittedColumns));
			}
			builder.add("column", population.columnList().stream().map(column -> builderOf(column).toFrame()).toArray(Frame[]::new));
		}
	}

	private Frame splitted(List<PopulationSource.Column> splittedColumns) {
		return new FrameBuilder("splittedColumns").add("column", splittedColumns.stream().map(column -> builderOf(column).add("splitted").toFrame()).toArray(Frame[]::new)).toFrame();
	}

	private FrameBuilder builderOf(PopulationSource.Column column) {
		FrameBuilder builder = new FrameBuilder("column")
				.add("fullName", column.tank().fullName())
				.add("name", column.tank().name$())
				.add("type", column.type().name())
				.add("mounter", column.core$().root().as(Mounter.class).name$());
		if (column.isId()) builder.add("facet", "id");
		else if (column.isTimetag()) builder.add("facet", "timetag");
		return builder;
	}

	private void realtimeMounter(FrameBuilder builder, Mounter mounter) {
		builder.add("realtime");
		if (!mounter.source().isEvents()) return;
		EventsSource eventsSource = mounter.source().asEvents();
		if (eventsSource.eventList().size() == 1) {
			Schema schema = eventsSource.eventList().get(0).schema();
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