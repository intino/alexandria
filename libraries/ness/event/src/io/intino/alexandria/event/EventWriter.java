package io.intino.alexandria.event;

import io.intino.alexandria.event.measurement.MeasurementEventWriter;
import io.intino.alexandria.event.message.MessageEventWriter;
import io.intino.alexandria.event.tuple.TupleEventWriter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

import static io.intino.alexandria.event.util.EventFormats.formatOf;

public interface EventWriter<T extends Event> {

	@SuppressWarnings("unchecked")
	static <T extends Event> EventWriter<T> of(File file) {
		switch(formatOf(file)) {
			case Message: return (EventWriter<T>) new MessageEventWriter(file);
			case Tuple: return (EventWriter<T>) new TupleEventWriter(file);
			case Measurement: return (EventWriter<T>) new MeasurementEventWriter(file);
		}
		return new Empty<>(); // TODO: throw exception instead?
	}

	void put(Stream<T> stream) throws IOException;

	default void put(Collection<T> messages) throws IOException {
		put(messages.stream());
	}

	class Empty<T extends Event> implements EventWriter<T> {
		@Override
		public void put(Stream<T> stream) {}
	}
}
