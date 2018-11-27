package io.intino.alexandria.triplestore;

import io.intino.alexandria.logger.Logger;

import java.io.*;

public class FileTripleStore extends MemoryTripleStore {
	private final File file;

	public FileTripleStore(File file) {
		super(inputStreamOf(file));
		this.file = file;
	}

	private static InputStream inputStreamOf(File file) {
		if (!file.exists()) return new ByteArrayInputStream(new byte[0]);
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException ignored) {
			return null;
		}
	}

	public File file() {
		return file;
	}

	public synchronized void save() {
		try {
			file.getParentFile().mkdirs();
			save(new FileOutputStream(this.file));
		} catch (FileNotFoundException e) {
			Logger.error(e);
		}
	}
}
