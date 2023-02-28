package io.intino.alexandria.event;

import io.intino.alexandria.event.measurement.MeasurementEventWriter;
import io.intino.alexandria.event.message.MessageEventWriter;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

import static io.intino.alexandria.event.util.EventFormats.formatOf;

public interface EventWriter<T extends Event> extends AutoCloseable {

	static <T extends Event> EventWriter<T> of(File file) throws IOException {
		return EventWriter.of(file, false);
	}

	static <T extends Event> EventWriter<T> of(File file, boolean append) throws IOException {
		return EventWriter.of(formatOf(file), IO.open(file, append));
	}

	@SuppressWarnings("unchecked")
	static <T extends Event> EventWriter<T> of(Event.Format format, OutputStream outputStream) throws IOException {
		switch(format) {
			case Message: return (EventWriter<T>) new MessageEventWriter(outputStream);
			case Measurement: return (EventWriter<T>) new MeasurementEventWriter(outputStream);
		}
		return new Empty<>(); // TODO: throw exception instead?
	}

	static <T extends Event> void append(File file, Stream<T> events) throws IOException {
		write(formatOf(file), IO.open(file, true), events);
	}

	static <T extends Event> void write(File file, Stream<T> events) throws IOException {
		write(formatOf(file), IO.open(file, false), events);
	}

	static <T extends Event> void write(Event.Format format, OutputStream destination, Stream<T> events) throws IOException {
		try(EventWriter<T> writer = EventWriter.of(format, destination)) {
			writer.write(events);
		}
	}

	static <T extends Event> void write(File file, Collection<T> events) throws IOException {
		write(formatOf(file), IO.open(file, false), events);
	}

	static <T extends Event> void append(File file, Collection<T> events) throws IOException {
		write(formatOf(file), IO.open(file, true), events);
	}

	static <T extends Event> void write(Event.Format format, OutputStream destination, Collection<T> events) throws IOException {
		try(EventWriter<T> writer = EventWriter.of(format, destination)) {
			writer.write(events);
		}
	}

	void write(T event) throws IOException;

	default void write(Stream<T> stream) throws IOException {
		try(stream) {
			Iterator<T> iterator = stream.iterator();
			while (iterator.hasNext()) write(iterator.next());
		}
	}

	default void write(Collection<T> messages) throws IOException {
		write(messages.stream());
	}

	void flush() throws IOException;

	@Override
	void close() throws IOException;

	class IO {
		public static OutputStream open(File file, boolean append) throws IOException {
			return new BufferedOutputStream(new FileOutputStream(file, append));
		}
	}

	class Empty<T extends Event> implements EventWriter<T> {
		@Override
		public void write(T event) {}
		@Override
		public void write(Stream<T> stream) {}

		@Override
		public void flush() {}

		@Override
		public void close() {}
	}
}
