package io.intino.alexandria.tabb.generators;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.tabb.ColumnStream;
import io.intino.alexandria.tabb.ColumnStream.Mode;
import io.intino.alexandria.tabb.ColumnStream.Type;
import io.intino.alexandria.tabb.FileGenerator;

import java.io.*;

import static io.intino.alexandria.tabb.ColumnStream.ColumnExtension;

public class TabbFileGenerator implements FileGenerator {
	private final ColumnStream stream;
	private final byte[] notAvailable;
	private File file;
	private OutputStream os;
	private long size = 0;

	public TabbFileGenerator(ColumnStream stream) {
		this.stream = stream;
		try {
			this.file = File.createTempFile("tabb_" + stream.name(), ColumnExtension);
			file.deleteOnExit();
			this.os = new BufferedOutputStream(new FileOutputStream(file));
		} catch (IOException e) {
			Logger.error(e);
		}
		this.notAvailable = stream.type().notAvailable();
	}

	public File file() {
		return file;
	}

	public void put(long key) {
		try {
			write(key() == key ? value() : notAvailable());
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	public void close() {
		try {
			os.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	public String name() {
		return stream.name();
	}

	public Type type() {
		return stream.type();
	}

	public Mode mode() {
		return stream.mode();
	}

	public long size() {
		return size;
	}

	private byte[] value() {
		return stream.key() != null ? type().toByteArray(stream.value()) : notAvailable;
	}

	private void write(byte[] b) throws IOException {
		os.write(b);
		size++;
	}

	private Long key() {
		return stream.key();
	}

	private byte[] notAvailable() {
		return notAvailable;
	}
}