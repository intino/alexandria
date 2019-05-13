package io.intino.alexandria.zet;

import io.intino.alexandria.logger.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class ZetWriter {
	private final File file;

	public ZetWriter(File file) {
		this.file = file;
		file.getParentFile().mkdirs();
	}

	public void write(long... data) {
		write(new ZetReader(data));
	}

	public void write(List<Long> messages) {
		write(new ZetReader(messages));
	}

	public void write(Stream<Long> stream) {
		write(new ZetReader(stream));
	}

	public void write(ZetStream stream) {
		try (ZOutputStream outputStream = zOutputStream(file)) {
			while (stream.hasNext()) outputStream.writeLong(stream.next());
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private ZOutputStream zOutputStream(File file) throws IOException {
		return new ZOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
	}

}