package io.intino.alexandria.datalake.file;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.LedReader;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileTransaction implements Datalake.TransactionStore.Transaction {
	private final File file;

	public FileTransaction(File file) {
		this.file = file;
	}

	@Override
	public Timetag timetag() {
		return new Timetag(file.getName().replace(FileTransactionStore.TransactionExtension, ""));
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

	public <T extends Transaction> LedStream<T> content(TransactionFactory<T> factory) {
		return new LedReader(file).read(factory);
	}
}