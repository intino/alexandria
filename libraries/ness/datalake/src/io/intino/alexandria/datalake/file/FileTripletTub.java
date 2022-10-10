package io.intino.alexandria.datalake.file;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.Datalake.TripletStore.Triplet;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Predicate;
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
		try {
			return Files.lines(tsv.toPath()).map(l -> new Triplet(l.split("\t")));
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
