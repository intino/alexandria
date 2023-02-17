package io.intino.alexandria.datalake.file.triplet;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FS;
import io.intino.alexandria.event.triplet.TripletEvent;

import java.io.File;
import java.util.stream.Stream;

public class TripletEventStore implements Datalake.Store<TripletEvent> {
	public static final String Extension = ".triplets";
	private final File root;

	public TripletEventStore(File root) {
		this.root = root;
		this.root.mkdirs();
	}

	@Override
	public Stream<Tank<TripletEvent>> tanks() {
		return FS.foldersIn(root).map(TripletEventTank::new);
	}

	@Override
	public TripletEventTank tank(String name) {
		return new TripletEventTank(new File(root, name));
	}
}
