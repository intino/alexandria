package io.intino.alexandria.datalake.file.triplet;

import io.intino.alexandria.datalake.Datalake.Store.Source;
import io.intino.alexandria.datalake.Datalake.Store.Tank;
import io.intino.alexandria.event.triplet.TripletEvent;

import java.io.File;

public class TripletEventTank implements Tank<TripletEvent> {
	private final File root;

	public TripletEventTank(File root) {
		this.root = root;
	}

	@Override
	public String name() {
		return root.getName();
	}

	@Override
	public Source<TripletEvent> source(String name) {
		return new TripletEventSource(new File(root, name));
	}

	public File root() {
		return root;
	}
}