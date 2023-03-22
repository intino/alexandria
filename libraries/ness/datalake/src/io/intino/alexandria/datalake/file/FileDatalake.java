package io.intino.alexandria.datalake.file;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.measurement.MeasurementEventStore;
import io.intino.alexandria.datalake.file.message.MessageEventStore;
import io.intino.alexandria.datalake.file.resource.ResourceEventStore;
import io.intino.alexandria.event.measurement.MeasurementEvent;
import io.intino.alexandria.event.message.MessageEvent;

import java.io.File;

public class FileDatalake implements Datalake {
	private final File root;

	public FileDatalake(File root) {
		this.root = root;
		checkStore();
	}

	private void checkStore() {
		messageStoreFolder().mkdirs();
		measurementStoreFolder().mkdirs();
		resourceStoreFolder().mkdirs();
	}

	@Override
	public Store<MessageEvent> messageStore() {
		return new MessageEventStore(messageStoreFolder());
	}

	@Override
	public Store<MeasurementEvent> measurementStore() {
		return new MeasurementEventStore(measurementStoreFolder());
	}

	@Override
	public ResourceStore resourceStore() {
		return new ResourceEventStore(resourceStoreFolder());
	}

	public File root() {
		return root;
	}

	public File messageStoreFolder() {
		return new File(root, MessageStoreFolder);
	}

	public File measurementStoreFolder() {
		return new File(root, MeasurementStoreFolder);
	}

	private File resourceStoreFolder() {
		return new File(root, ResourceStoreFolder);
	}
}