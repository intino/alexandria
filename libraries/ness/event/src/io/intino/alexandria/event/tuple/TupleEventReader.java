package io.intino.alexandria.event.tuple;

import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.ztp.Tuple;
import io.intino.alexandria.ztp.TupleReader;
import io.intino.alexandria.ztp.ZtpStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class TupleEventReader implements EventReader<TupleEvent> {

	private final Iterator<TupleEvent> iterator;

	public TupleEventReader(File file) throws IOException {
		this(new ZtpToEventIterator(ZtpStream.of(file).iterator()));
	}

	public TupleEventReader(InputStream is) throws IOException {
		this(new ZtpToEventIterator(ZtpStream.of(is).iterator()));
	}

	public TupleEventReader(String text) {
		this(new ZtpToEventIterator(new TupleReader(text)));
	}

	public TupleEventReader(TupleEvent... events) {
		this(stream(events));
	}

	public TupleEventReader(List<TupleEvent> events) {
		this(events.stream());
	}

	public TupleEventReader(Stream<TupleEvent> stream) {
		this(stream.sorted().iterator());
	}

	public TupleEventReader(Iterator<TupleEvent> iterator) {
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public TupleEvent next() {
		return iterator.next();
	}

	@Override
	public void close() throws Exception {
		if(iterator instanceof AutoCloseable) ((AutoCloseable) iterator).close();
	}

	private static class ZtpToEventIterator implements Iterator<TupleEvent>, AutoCloseable {

		private final Iterator<Tuple> source;

		public ZtpToEventIterator(Iterator<Tuple> source) {
			this.source = source;
		}

		@Override
		public void close() throws Exception {
			if(source instanceof AutoCloseable) ((AutoCloseable) source).close();
		}

		@Override
		public boolean hasNext() {
			return source.hasNext();
		}

		@Override
		public TupleEvent next() {
			return new TupleEvent(source.next());
		}
	}
}
