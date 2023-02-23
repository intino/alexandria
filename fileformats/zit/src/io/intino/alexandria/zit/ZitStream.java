package io.intino.alexandria.zit;

import io.intino.alexandria.iteratorstream.ResourceIteratorStream;
import io.intino.alexandria.zit.model.Data;

import java.io.*;

import static java.util.Objects.requireNonNull;

@SuppressWarnings({"all"})
public class ZitStream extends ResourceIteratorStream<Data> {

	public static ZitStream of(InputStream is) throws IOException {
		return new ZitStream(readerOf(Zit.decompressing(is)));
	}

	public static ZitStream of(ItsReader reader) {
		return new ZitStream(reader);
	}

	public static ZitStream of(File file) throws IOException {
		return new ZitStream(readerOf(Zit.decompressing(fileInputStream(file))));
	}

	private final ItsReader reader;

	public ZitStream(ItsReader reader) {
		super(reader.data().iterator());
		this.reader = requireNonNull(reader);
	}

	public String sensor() {
		return reader.sensor();
	}

	private static ItsReader readerOf(InputStream is) {
		return new ItsReader(is);
	}

	private static BufferedInputStream fileInputStream(File file) throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(file));
	}
}