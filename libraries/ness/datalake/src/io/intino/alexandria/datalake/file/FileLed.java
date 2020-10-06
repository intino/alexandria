package io.intino.alexandria.datalake.file;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.LedReader;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileLed implements Datalake.LedgerStore.Led {
	private final File file;

	public FileLed(File file) {
		this.file = file;
	}

	@Override
	public Timetag timetag() {
		return new Timetag(file.getName().replace(FileLedgerStore.LedExtension, ""));
	}

	public File file() {
		return file;
	}

	@Override
	public int size() {
		try {
			return new LedReader(new FileInputStream(file)).size();
		} catch (IOException e) {
			Logger.error(e);
			return 0;
		}
	}

	@Override
	public <T extends Schema> Led<T> content(Class<T> clazz) {
		try {
			return new LedReader(new FileInputStream(file)).read(clazz);
		} catch (FileNotFoundException e) {
			Logger.error(e);
			return null;
		}
	}
}
