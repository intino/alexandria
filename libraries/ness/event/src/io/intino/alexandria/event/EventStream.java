package io.intino.alexandria.event;

import io.intino.alexandria.iteratorstream.ResourceIteratorStream;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;

public class EventStream<T extends Event> extends ResourceIteratorStream<T> {

	public static <T extends Event> Stream<T> sequence(Stream<Stream<T>> streams) {
		return streams.flatMap(Function.identity());
	}

	public static <T extends Event> Stream<T> merge(Stream<Stream<T>> streams) {
		return new EventStream<>(new MergeIterator<>(streams));
	}

	public static <T extends Event> Stream<T> of(File file) throws IOException {
		return new EventStream<>(EventReader.of(file));
	}

	public EventStream(Iterator<T> iterator) {
		super(iterator);
	}

	@SuppressWarnings({"unchecked", "unused"})
	private static class MergeIterator<T extends Event> implements Iterator<T>, AutoCloseable {
		private final Iterator<T>[] inputs;
		private final Event[] current;

		public MergeIterator(Stream<Stream<T>> streams) {
			this.inputs = streams.map(Stream::iterator).toArray(Iterator[]::new);
			this.current = stream(inputs).map(this::next).toArray(Event[]::new);
		}

		@Override
		public boolean hasNext() {
			return !stream(current).allMatch(Objects::isNull);
		}

		@Override
		public T next() {
			return next(minIndex());
		}

		private T next(int index) {
			T message = (T) current[index];
			current[index] = next(inputs[index]);
			return message;
		}

		private T next(Iterator<T> input) {
			return input.hasNext() ? input.next() : null;
		}

		private int minIndex() {
			return range(0, current.length).boxed().min(this::comparingTimestamp).orElse(-1);
		}

		private int comparingTimestamp(int a, int b) {
			return tsOf(current[a]).compareTo(tsOf(current[b]));
		}

		private Instant tsOf(Event event) {
			return event != null ? event.ts() : Instant.MAX;
		}

		@Override
		public void close() throws Exception {
			Exception e = null;
			for (Iterator<T> iterator : inputs) {
				e = tryClose(iterator);
			}
			if (e != null) throw e;
		}

		private Exception tryClose(Iterator<T> iterator) {
			if (iterator instanceof AutoCloseable) {
				try {
					((AutoCloseable) iterator).close();
				} catch (Exception e) {
					return e;
				}
			}
			return null;
		}
	}
}
