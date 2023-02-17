package io.intino.alexandria.datalake.file;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.message.MessageEventStore;
import io.intino.alexandria.datalake.file.triplet.TripletEventStore;
import io.intino.alexandria.event.measurement.MeasurementEvent;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.event.triplet.TripletEvent;

import java.io.File;

public class FileDatalake implements Datalake {
	private final File root;

	public FileDatalake(File root) {
		this.root = root;
		checkStore();
	}

	private void checkStore() {
		tripletStoreFolder().mkdirs();
		eventStoreFolder().mkdirs();
	}

	@Override
	public Store<MessageEvent> messageStore() {
		return new MessageEventStore(eventStoreFolder());
	}

	@Override
	public Store<TripletEvent> tripletStore() {
		return new TripletEventStore(tripletStoreFolder());
	}

	@Override
	public Store<MeasurementEvent> measurementStore() {
		return null;
	}

	public File root() {
		return root;
	}

	public File eventStoreFolder() {
		return new File(root, EventStoreFolder);
	}

	public File tripletStoreFolder() {
		return new File(root, TripletStoreFolder);
	}

}