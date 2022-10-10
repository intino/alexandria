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
		tripletStoreFolder().mkdirs();
		eventStoreFolder().mkdirs();
	}

	@Override
	public FileEventStore eventStore() {
		return new FileEventStore(eventStoreFolder());
	}

	@Override
	public FileTripletStore tripletsStore() {
		return new FileTripletStore(tripletStoreFolder());
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