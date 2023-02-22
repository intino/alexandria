package io.intino.alexandria.event.measurement;

import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.zit.ItzStream;
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
		this(new ItzToEventIterator(ItzStream.of(file)));
	}

	public MeasurementEventReader(InputStream is) {
		this(new ItzToEventIterator(ItzStream.of(is)));
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

	private static class ItzToEventIterator implements Iterator<MeasurementEvent>, AutoCloseable {

		private final ItzStream source;

		public ItzToEventIterator(ItzStream source) {
			this.source = source;
		}

		@Override
		public void close() throws Exception {
			if (source != null) ((AutoCloseable) source).close();
		}

		@Override
		public boolean hasNext() {
			return source.hasNext();
		}

		@Override
		public MeasurementEvent next() {
			Data next = source.next();
			return new MeasurementEvent(next.ts(), source.sensor(), source.measuements(), next.values());
		}
	}
}
