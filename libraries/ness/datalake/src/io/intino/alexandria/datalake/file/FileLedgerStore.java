package io.intino.alexandria.datalake.file;

import io.intino.alexandria.datalake.Datalake;

import java.io.File;
import java.util.stream.Stream;

public class FileLedgerStore implements Datalake.LedgerStore {
	public static final String LedExtension = ".led";
	private final File root;

	public FileLedgerStore(File root) {
		this.root = root;
		this.root.mkdirs();
	}

	@Override
	public Stream<Tank> tanks() {
		return FS.foldersIn(root).map(FileLedTank::new);
	}

	@Override
	public FileLedTank tank(String name) {
		return new FileLedTank(new File(root, name));
	}


}
