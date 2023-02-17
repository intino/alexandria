package io.intino.alexandria.datalake.file.triplet;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.triplet.TripletEvent;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TripletEventTub implements Datalake.Store.Tub<TripletEvent> {
	private final File tsv;

	public TripletEventTub(File tsv) {
		this.tsv = tsv;
	}

	public String name() {
		return tsv.getName().replace(TripletEventStore.Extension, "");
	}

	@Override
	public Timetag timetag() {
		return new Timetag(name());
	}

	@Override
	public Stream<TripletEvent> events() {
		try(Stream<String> lines = Files.lines(tsv.toPath())) {
			return lines.filter(line -> !line.isEmpty()).map(l -> new TripletEvent(l.split("\t", -1))).collect(Collectors.toList()).stream();
		} catch (IOException e) {
			Logger.error(e);
			return Stream.empty();
		}
	}

}
