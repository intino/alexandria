package io.intino.alexandria.datalake.file;

import io.intino.alexandria.datalake.Datalake;

import java.io.File;
import java.util.stream.Stream;

public class FileSetStore implements Datalake.SetStore {
	public static final String SetExtension = ".zet";
	public static final String MetadataFilename = ".metadata";
	public static final String IndexFileName = ".mapp";

	private final File root;

	public FileSetStore(File root) {
		this.root = root;
		this.root.mkdirs();
	}

	@Override
	public Stream<Tank> tanks() {
		return FS.foldersIn(root).map(FileSetTank::new);
	}

	@Override
	public FileSetTank tank(String name) {
		return new FileSetTank(new File(root, name));
	}
}
