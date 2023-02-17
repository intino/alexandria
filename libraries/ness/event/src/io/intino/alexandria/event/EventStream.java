package io.intino.alexandria.event;

import java.io.File;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;

public class EventStream<T extends Event> extends AbstractEventStream<T> implements Iterator<T>, AutoCloseable {

	public static <T extends Event> Stream<T> sequence(Stream<T>... streams) {
		return Arrays.stream(streams).flatMap(Function.identity());
	}

	public static <T extends Event> Stream<T> merge(Stream<T>... streams) {
		return new EventStream<>(new MergeIterator<>(streams));
	}

	public static <T extends Event> Stream<T> of(File file) {
		return new EventStream<>(FileEventReader.events(file).iterator());
	}

	private final Iterator<T> iterator;
	private final List<Runnable> closeHandlers;

	public EventStream(Iterator<T> iterator) {
		this.iterator = iterator;
		this.closeHandlers = new LinkedList<>();
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public T next() {
		return iterator.next();
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		try {
			while(hasNext()) {
				action.accept(next());
			}
		} finally {
			close();
		}
	}

	@Override
	public Stream<T> onClose(Runnable closeHandler) {
		if(closeHandler != null) this.closeHandlers.add(closeHandler);
		return this;
	}

	@Override
	public void close() {
		closeIterator(iterator);
		closeHandlers.forEach(Runnable::run);
	}

	private static void closeIterator(Iterator<?> iterator) {
		if(iterator instanceof AutoCloseable) {
			try {
				((AutoCloseable) iterator).close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@SuppressWarnings({"unchecked", "unused"})
	private static class MergeIterator<T extends Event> implements Iterator<T>, AutoCloseable {

		private final Iterator<T>[] inputs;
		private final Event[] current;

		public MergeIterator(Stream<T>... streams) {
			this.inputs = Arrays.stream(streams).map(Stream::iterator).toArray(Iterator[]::new);
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
			for(Iterator<T> iterator : inputs) {
				e = tryClose(iterator);
			}
			if(e != null) throw e;
		}

		private Exception tryClose(Iterator<T> iterator) {
			if(iterator instanceof AutoCloseable) {
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
