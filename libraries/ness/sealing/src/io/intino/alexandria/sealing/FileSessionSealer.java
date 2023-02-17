package io.intino.alexandria.sealing;

import io.intino.alexandria.datalake.Datalake.Store.Tank;
import io.intino.alexandria.datalake.file.FileDatalake;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.logger.Logger;

import java.io.File;
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
	public synchronized void seal(List<Tank<? extends Event>> avoidSorting) {
		try {
			sealEvents(avoidSorting);
		} catch (Throwable e) {
			Logger.error(e);
		}
	}

	private void sealEvents(List<Tank<? extends Event>> avoidSorting) {
		EventSessionSealer.seal(stageFolder, datalake.eventStoreFolder(), avoidSorting, tempFolder);
	}

	private static File tempFolder(File stageFolder) {
		File temp = new File(stageFolder, "temp");
		temp.mkdir();
		return temp;
	}
}