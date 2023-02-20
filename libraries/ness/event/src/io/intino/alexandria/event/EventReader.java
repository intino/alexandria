package io.intino.alexandria.event;

import io.intino.alexandria.event.measurement.MeasurementEventReader;
import io.intino.alexandria.event.message.MessageEventReader;
import io.intino.alexandria.event.tuple.TupleEventReader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static io.intino.alexandria.event.util.EventFormats.formatOf;

public interface EventReader<T extends Event> extends Iterator<T>, AutoCloseable {

	@SuppressWarnings("unchecked")
	static <T extends Event> EventReader<T> of(File file) throws IOException {
		switch(formatOf(file)) {
			case Message: return (EventReader<T>) new MessageEventReader(file);
			case Tuple: return (EventReader<T>) new TupleEventReader(file);
			case Measurement: return (EventReader<T>) new MeasurementEventReader(file);
		}
		return new Empty<>(); // TODO: throw exception instead?
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
