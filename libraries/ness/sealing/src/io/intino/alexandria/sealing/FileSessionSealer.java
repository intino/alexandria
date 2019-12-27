package io.intino.alexandria.sealing;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FileDatalake;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileSessionSealer implements SessionSealer {
	private final File stageFolder;
	private final FileStage stage;
	private final FileDatalake datalake;

	public FileSessionSealer(FileDatalake datalake, File stageFolder) {
		this.datalake = datalake;
		this.stageFolder = stageFolder;
		this.stage = new FileStage(stageFolder);
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
		sealEvents(avoidSorting);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			Logger.error(e);
		}
		sealSets();
		makeSetIndexes();
		stage.clear();
		unlock();
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

	private boolean isSealing() {
		return lockFile().exists();
	}

	private File lockFile() {
		return new File(datalake.root(), ".lock");
	}

	private void makeSetIndexes() {
		new SetIndexer(datalake.setStoreFolder()).make();
	}

	private void sealSets() {
		SetSessionManager.seal(stageFolder, datalake.setStoreFolder(), tempFolder());
	}

	private void sealEvents(List<Datalake.EventStore.Tank> avoidSorting) {
		EventSessionManager.seal(stageFolder, datalake.eventStoreFolder(), avoidSorting, tempFolder());
	}

	private File tempFolder() {
		File temp = new File(this.stageFolder, "temp");
		temp.mkdir();
		return temp;
	}
}