package io.intino.alexandria.sealing;

import io.intino.alexandria.datalake.Datalake.Store.Tank;
import io.intino.alexandria.datalake.file.FileDatalake;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.util.function.Predicate;

public class FileSessionSealer implements SessionSealer {
	private final File stageDir;
	private final FileDatalake datalake;
	private final File tempDir;
	private final File treatedDir;

	public FileSessionSealer(FileDatalake datalake, File stageDir, File treatedDir) {
		this(datalake, stageDir, tempFolder(stageDir), treatedDir);
	}

	public FileSessionSealer(FileDatalake datalake, File stageDir, File tempDir, File treatedDir) {
		this.datalake = datalake;
		this.stageDir = stageDir;
		this.tempDir = tempDir;
		this.treatedDir = treatedDir;
	}

	@Override
	public synchronized void seal(Predicate<Tank<? extends Event>> sortingPolicy) {
		try {
			sealEvents(sortingPolicy);
		} catch (Throwable e) {
			Logger.error(e);
		}
	}

	private void sealEvents(Predicate<Tank<? extends Event>> sortingPolicy) {
		new EventSessionSealer(datalake, stageDir, tempDir, treatedDir).seal(t -> check(t, sortingPolicy));
	}

	private boolean check(String tank, Predicate<Tank<? extends Event>> sortingPolicy) {
		return sortingPolicy.test(datalake.messageStore().tank(tank))
				|| sortingPolicy.test(datalake.tupleStore().tank(tank))
				|| sortingPolicy.test(datalake.measurementStore().tank(tank));
	}

	private static File tempFolder(File stageFolder) {
		File temp = new File(stageFolder, "temp");
		temp.mkdir();
		return temp;
	}
}