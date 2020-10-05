package io.intino.alexandria.datalake.file;

import io.intino.alexandria.datalake.Datalake;

import java.io.File;

public class FileDatalake implements Datalake {
	private final File root;

	public FileDatalake(File root) {
		this.root = root;
		checkStore();
	}

	private void checkStore() {
		setStoreFolder().mkdirs();
		eventStoreFolder().mkdirs();
	}

	@Override
	public FileEventStore eventStore() {
		return new FileEventStore(eventStoreFolder());
	}

	@Override
	public FileSetStore setStore() {
		return new FileSetStore(setStoreFolder());
	}

	@Override
	public LedStore ledStore() {
		return new FileLedStore(ledStoreFolder());
	}

	public File root() {
		return root;
	}

	public File eventStoreFolder() {
		return new File(root, EventStoreFolder);
	}

	public File setStoreFolder() {
		return new File(root, SetStoreFolder);
	}

	public File ledStoreFolder() {
		return new File(root, SetStoreFolder);
	}
}