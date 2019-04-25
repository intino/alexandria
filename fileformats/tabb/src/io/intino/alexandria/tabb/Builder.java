package io.intino.alexandria.tabb;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.tabb.ColumnStream.Mode;
import io.intino.alexandria.tabb.ColumnStream.Type;

import java.io.*;

import static io.intino.alexandria.tabb.ColumnStream.ColumnExtension;

class Builder implements Generator {
	private final ColumnStream stream;
	private final byte[] notAvailable;
	private File file;
	private OutputStream os;
	private long size = 0;

	Builder(ColumnStream stream) {
		this.stream = stream;
		try {
			this.file = File.createTempFile("tabb_" + stream.name(), ColumnExtension);
			file.deleteOnExit();
			this.os = new BufferedOutputStream(new FileOutputStream(file));
		} catch (IOException e) {
			Logger.error(e);
		}
		this.notAvailable = ColumnStream.NotAvailable.bytesOf(stream.type());
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

	String name() {
		return stream.name();
	}

	Type type() {
		return stream.type();
	}

	Mode mode() {
		return stream.mode();
	}

	long size() {
		return size;
	}

	private void write(byte[] b) throws IOException {
		os.write(b);
		size++;
	}

	private Long key() {
		return stream.key();
	}

	private byte[] value() {
		return stream.key() != null ? stream.value() : notAvailable;
	}

	private byte[] notAvailable() {
		return notAvailable;
	}
}