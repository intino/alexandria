package io.intino.alexandria.event.measurement;

import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.zit.ZitStream;
import io.intino.alexandria.zit.model.Data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class MeasurementEventReader implements EventReader<MeasurementEvent> {
	private final Iterator<MeasurementEvent> iterator;

	public MeasurementEventReader(File file) throws IOException {
		this(new ZitToEventIterator(ZitStream.of(file)));
	}

	public MeasurementEventReader(InputStream is) throws IOException {
		this(new ZitToEventIterator(ZitStream.of(is)));
	}

	public MeasurementEventReader(MeasurementEvent... events) {
		this(stream(events));
	}

	public MeasurementEventReader(List<MeasurementEvent> events) {
		this(events.stream());
	}

	public MeasurementEventReader(Stream<MeasurementEvent> stream) {
		this(stream.sorted().iterator());
	}

	public MeasurementEventReader(Iterator<MeasurementEvent> iterator) {
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public MeasurementEvent next() {
		return iterator.next();
	}

	@Override
	public void close() throws Exception {
		if (iterator instanceof AutoCloseable) ((AutoCloseable) iterator).close();
	}

	private static class ZitToEventIterator implements Iterator<MeasurementEvent>, AutoCloseable {

		private final ZitStream stream;

		public ZitToEventIterator(ZitStream stream) {
			this.stream = stream;
		}

		@Override
		public void close() throws Exception {
			if (stream != null) ((AutoCloseable) stream).close();
		}

		@Override
		public boolean hasNext() {
			return stream.hasNext();
		}

		@Override
		public MeasurementEvent next() {
			Data next = stream.next();
			return new MeasurementEvent(stream.id(), stream.id(), next.ts(), next.measurements(), next.values());
		}
	}
}
