package io.intino.alexandria.ztp;

import io.intino.alexandria.iteratorstream.ResourceIteratorStream;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

public class ZtpStream extends ResourceIteratorStream<Tuple> {

	public static ZtpStream sequence(File first, File... rest) throws IOException {
		ZtpStream[] streams = new ZtpStream[1 + rest.length];
		streams[0] = ZtpStream.of(first);
		for (int i = 0; i < rest.length; i++) streams[i + 1] = ZtpStream.of(rest[i]);
		return new ZtpStream(Arrays.stream(streams).flatMap(Function.identity()).iterator());
	}

	public static ZtpStream sequence(Stream<Tuple>... streams) {
		return new ZtpStream(Arrays.stream(streams).flatMap(Function.identity()).iterator());
	}

	public static ZtpStream of(File file) throws IOException {
		return new ZtpStream(!file.exists() ? Collections.emptyIterator() : readerOf(Ztp.decompressing(fileInputStream(file))));
	}

	public static ZtpStream of(InputStream is) throws IOException {
		return new ZtpStream(readerOf(Ztp.decompressing(is)));
	}

	public static ZtpStream of(String text) {
		return ZtpStream.of(new TupleReader(text));
	}

	public static ZtpStream of(Tuple... tuples) {
		return new ZtpStream(Arrays.stream(tuples).iterator());
	}

	public static ZtpStream of(Collection<Tuple> tuples) {
		return new ZtpStream(tuples.iterator());
	}

	public static ZtpStream of(Stream<Tuple> tuples) {
		return new ZtpStream(tuples.iterator());
	}

	public static ZtpStream of(TupleReader reader) {
		return new ZtpStream(reader);
	}

	public ZtpStream(Iterator<Tuple> iterator) {
		super(iterator);
	}

	private static TupleReader readerOf(InputStream is) {
		return new TupleReader(is);
	}

	private static BufferedInputStream fileInputStream(File file) throws IOException {
		return new BufferedInputStream(new FileInputStream(file));
	}
}
