package io.intino.alexandria.sealing;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FileDatalake;

import java.io.File;
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
	public void seal(List<Datalake.EventStore.Tank> avoidSorting) {
		sealEvents(avoidSorting);
		sealSets();
		makeSetIndexes();
		stage.clear();

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