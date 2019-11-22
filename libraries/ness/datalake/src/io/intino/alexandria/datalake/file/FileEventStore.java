package io.intino.alexandria.datalake.file;

import io.intino.alexandria.datalake.Datalake;

import java.io.File;
import java.util.stream.Stream;

import static io.intino.alexandria.zim.ZimReader.ZimExtension;

public class FileEventStore implements Datalake.EventStore {
	public static final String EventExtension = ZimExtension;
	private File root;

	public FileEventStore(File root) {
		this.root = root;
	}

	@Override
	public Stream<Tank> tanks() {
		return FS.foldersIn(root).map(FileEventTank::new);
	}

	public File root() {
		return root;
	}

	@Override
	public FileEventTank tank(String name) {
		return new FileEventTank(new File(root, name));
	}

}
