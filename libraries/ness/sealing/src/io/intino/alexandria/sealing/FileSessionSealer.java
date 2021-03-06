package io.intino.alexandria.sealing;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FileDatalake;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileSessionSealer implements SessionSealer {
	private final File stageFolder;
	private final FileDatalake datalake;
	private final File tempFolder;

	public FileSessionSealer(FileDatalake datalake, File stageFolder) {
		this(datalake, stageFolder, tempFolder(stageFolder));
	}

	public FileSessionSealer(FileDatalake datalake, File stageFolder, File tempFolder) {
		this.datalake = datalake;
		this.stageFolder = stageFolder;
		this.tempFolder = tempFolder;
	}

	@Override
	public synchronized void seal(List<Datalake.EventStore.Tank> avoidSorting) {
		if (isSealing()) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		lock();
		try {
			sealEvents(avoidSorting);
			sealSets();
			makeSetIndexes();
		} catch (Throwable e) {
			Logger.error(e);
		}
		unlock();
	}

	private boolean isSealing() {
		return lockFile().exists();
	}

	private void sealEvents(List<Datalake.EventStore.Tank> avoidSorting) {
		EventSessionManager.seal(stageFolder, datalake.eventStoreFolder(), avoidSorting, tempFolder);
	}

	private void sealSets() {
		SetSessionManager.seal(stageFolder, datalake.setStoreFolder(), tempFolder);
	}

	private void makeSetIndexes() {
		new SetIndexer(datalake.setStoreFolder()).make();
	}

	private void lock() {
		try {
			lockFile().createNewFile();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void unlock() {
		lockFile().delete();
		notify();
	}

	private File lockFile() {
		return new File(datalake.root(), ".lock");
	}

	private static File tempFolder(File stageFolder) {
		File temp = new File(stageFolder, "temp");
		temp.mkdir();
		return temp;
	}
}