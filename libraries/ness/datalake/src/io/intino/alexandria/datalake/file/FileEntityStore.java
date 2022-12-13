package io.intino.alexandria.datalake.file;

import io.intino.alexandria.datalake.Datalake;

import java.io.File;
import java.util.stream.Stream;

public class FileEntityStore implements Datalake.EntityStore {
	public static final String Extension = ".triplets";
	private final File root;

	public FileEntityStore(File root) {
		this.root = root;
		this.root.mkdirs();
	}

	@Override
	public Stream<Tank> tanks() {
		return FS.foldersIn(root).map(FileEntityTank::new);
	}

	@Override
	public FileEntityTank tank(String name) {
		return new FileEntityTank(new File(root, name));
	}
}
