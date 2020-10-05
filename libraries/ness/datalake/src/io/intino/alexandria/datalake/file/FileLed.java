package io.intino.alexandria.datalake.file;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.led.Item;
import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.LedReader;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.zet.ZFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileLed implements Datalake.LedStore.Led {
	private final File file;

	public FileLed(File file) {
		this.file = file;
	}

	public String name() {
		return file.getName().replace(FileLedStore.LedExtension, "");
	}

	@Override
	public Timetag timetag() {
		return new Timetag(file.getParentFile().getName());
	}

	public File file() {
		return file;
	}

	@Override
	public int size() {
		try {
			return file.exists() ? (int) new ZFile(file).size() : 0;
		} catch (IOException e) {
			Logger.error(e);
			return 0;
		}
	}

	@Override
	public <T extends Item> Led<T> content(Class<T> clazz) {
		try {
			return new LedReader(new FileInputStream(file)).read(clazz);
		} catch (FileNotFoundException e) {
			Logger.error(e);
			return null;
		}
	}
}
