package io.intino.alexandria.zit;

import io.intino.alexandria.iteratorstream.ResourceIteratorStream;
import io.intino.alexandria.zit.model.Data;
import io.intino.alexandria.zit.model.Period;

import java.io.*;

import static java.util.Objects.requireNonNull;

@SuppressWarnings({"all"})
public class ZitStream extends ResourceIteratorStream<Data> {

	public static ZitStream of(InputStream is) throws IOException {
		return new ZitStream(readerOf(Zit.decompressing(is)));
	}

	public static ZitStream of(ItlReader reader) {
		return new ZitStream(reader);
	}

	public static ZitStream of(File file) throws IOException {
		return new ZitStream(readerOf(Zit.decompressing(fileInputStream(file))));
	}

	private final ItlReader reader;

	public ZitStream(ItlReader reader) {
		super(reader.data().iterator());
		this.reader = requireNonNull(reader);
	}

	public String id() {
		return reader.id();
	}


	public String sensor() {
		return reader.sensor();
	}

	public Period period() {
		return reader.period();
	}

	public String[] measurements() {
		return reader.measurements();
	}

	private static ItlReader readerOf(InputStream is) {
		return new ItlReader(is);
	}

	private static BufferedInputStream fileInputStream(File file) throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(file));
	}
}