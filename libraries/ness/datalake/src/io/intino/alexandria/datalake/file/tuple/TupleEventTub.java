package io.intino.alexandria.datalake.file.tuple;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.tuple.TupleEvent;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TupleEventTub implements Datalake.Store.Tub<TupleEvent> {
	private final File tsv;

	public TupleEventTub(File tsv) {
		this.tsv = tsv;
	}

	public String name() {
		return tsv.getName().replace(TupleEventStore.Extension, "");
	}

	@Override
	public Timetag timetag() {
		return new Timetag(name());
	}

	@Override
	public Stream<TupleEvent> events() {
		try(Stream<String> lines = Files.lines(tsv.toPath())) {
			return lines.filter(line -> !line.isEmpty()).map(l -> new TupleEvent(l.split("\t", -1))).collect(Collectors.toList()).stream();
		} catch (IOException e) {
			Logger.error(e);
			return Stream.empty();
		}
	}

}
