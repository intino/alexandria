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
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			Logger.error(e);
			return new ByteArrayInputStream(new byte[0]);
		}
	}

	public File file() {
		return file;
	}

	public synchronized void save() {
		try {
			save(new FileOutputStream(this.file));
		} catch (FileNotFoundException e) {
			Logger.error(e);
		}
	}

}
