package io.intino.alexandria.datalake.file.tuple;

import io.intino.alexandria.datalake.Datalake.Store.Source;
import io.intino.alexandria.datalake.Datalake.Store.Tank;
import io.intino.alexandria.event.triplet.TripletEvent;

import java.io.File;

public class TupleEventTank implements Tank<TripletEvent> {
	private final File root;

	public TupleEventTank(File root) {
		this.root = root;
	}

	@Override
	public String name() {
		return root.getName();
	}

	@Override
	public Source<TripletEvent> source(String name) {
		return new TupleEventSource(new File(root, name));
	}

	public File root() {
		return root;
	}
}