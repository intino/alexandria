package io.intino.alexandria.event;

import io.intino.alexandria.event.measurement.MeasurementEventReader;
import io.intino.alexandria.event.message.MessageEventReader;
import io.intino.alexandria.event.tuple.TupleEventReader;

import java.io.*;
import java.util.Iterator;

import static io.intino.alexandria.event.util.EventFormats.formatOf;

public interface EventReader<T extends Event> extends Iterator<T>, AutoCloseable {

	static <T extends Event> EventReader<T> of(File file) throws IOException {
		if(!file.exists()) return new Empty<>();
		return EventReader.of(formatOf(file), IO.open(file));
	}

	@SuppressWarnings("unchecked")
	static <T extends Event> EventReader<T> of(Event.Format format, InputStream inputStream) throws IOException {
		switch(format) {
			case Message: return (EventReader<T>) new MessageEventReader(inputStream);
			case Tuple: return (EventReader<T>) new TupleEventReader(inputStream);
			case Measurement: return (EventReader<T>) new MeasurementEventReader(inputStream);
		}
		return new Empty<>(); // TODO: throw exception instead?
	}

	class IO {
		public static InputStream open(File file) throws IOException {
			return new BufferedInputStream(new FileInputStream(file));
		}
	}

	class Empty<T extends Event> implements EventReader<T> {
		@Override
		public void close() {}

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public T next() {
			return null;
		}
	}
}
