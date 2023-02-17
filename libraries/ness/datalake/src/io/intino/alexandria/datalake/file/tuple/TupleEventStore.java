package io.intino.alexandria.datalake.file.tuple;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.event.triplet.TripletEvent;

import java.io.File;
import java.util.stream.Stream;

public class TupleEventStore implements Datalake.Store<TripletEvent> {
	public static final String Extension = ".tuples";
	private final File root;

	public TupleEventStore(File root) {
		this.root = root;
		this.root.mkdirs();
	}

	@Override
	public Stream<Tank<TripletEvent>> tanks() {
		return FS.foldersIn(root).map(TupleEventTank::new);
	}

	@Override
	public TupleEventTank tank(String name) {
		return new TupleEventTank(new File(root, name));
	}
}
