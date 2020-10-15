package io.intino.alexandria.datalake.file;

import io.intino.alexandria.datalake.Datalake;

import java.io.File;
import java.util.stream.Stream;

public class FileTransactionStore implements Datalake.TransactionStore {
	public static final String LedExtension = ".led";
	private final File root;

	public FileTransactionStore(File root) {
		this.root = root;
		this.root.mkdirs();
	}

	@Override
	public Stream<Tank> tanks() {
		return FS.foldersIn(root).map(FileTransactionTank::new);
	}

	@Override
	public FileTransactionTank tank(String name) {
		return new FileTransactionTank(new File(root, name));
	}
}
