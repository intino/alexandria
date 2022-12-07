package io.intino.alexandria.datalake.file;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.Datalake.TripletStore.Triplet;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileTripletTub implements Datalake.TripletStore.Tub {

	private final File tsv;

	public FileTripletTub(File tsv) {
		this.tsv = tsv;
	}

	public String name() {
		return tsv.getName().replace(FileTripletStore.Extension, "");
	}

	@Override
	public Timetag timetag() {
		return new Timetag(name());
	}

	@Override
	public Scale scale() {
		return timetag().scale();
	}

	@Override
	public Stream<Triplet> triplets() {
		try(Stream<String> lines = Files.lines(tsv.toPath())) {
			return lines.filter(line -> !line.isEmpty()).map(l -> new Triplet(l.split("\t", -1))).collect(Collectors.toList()).stream();
		} catch (IOException e) {
			Logger.error(e);
			return Stream.empty();
		}
	}

	@Override
	public Stream<Triplet> triplets(Predicate<Triplet> filter) {
		return triplets().filter(filter);
	}
}
