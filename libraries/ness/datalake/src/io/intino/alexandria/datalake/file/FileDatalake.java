package io.intino.alexandria.datalake.file;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.measurement.MeasurementEventStore;
import io.intino.alexandria.datalake.file.message.MessageEventStore;
import io.intino.alexandria.datalake.file.tuple.TupleEventStore;
import io.intino.alexandria.event.measurement.MeasurementEvent;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.event.tuple.TupleEvent;

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
	public Store<TupleEvent> tupleStore() {
		return new TupleEventStore(tripletStoreFolder());
	}

	@Override
	public Store<MeasurementEvent> measurementStore() {
		return new MeasurementEventStore(measurementStoreFolder());
	}

	public File root() {
		return root;
	}

	public File eventStoreFolder() {
		return new File(root, MessageStoreFolder);
	}

	public File tripletStoreFolder() {
		return new File(root, TupleStoreFolder);
	}

	public File measurementStoreFolder() {
		return new File(root, MeasurementStoreFolder);
	}

}