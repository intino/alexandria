package io.intino.alexandria.led;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface LedStream<T extends Schema> extends Iterator<T>, AutoCloseable {
	int schemaSize();

	class Filter<T extends Schema> implements LedStream<T> {
		private final LedStream<T> led;
		private final Predicate<T> predicate;

		public Filter(LedStream<T> ledStream, Predicate<T> predicate) {
			this.led = ledStream;
			this.predicate = predicate;
		}

		@Override
		public int schemaSize() {
			return led.schemaSize();
		}

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public T next() {
			return null;
		}

		@Override
		public void close() {
		}
	}

	class Join<T extends Schema> implements LedStream<T> {
		private final List<LedStream<T>> leds;

		public Join(List<LedStream<T>> leds) {
			this.leds = leds;
		}

		@Override
		public int schemaSize() {
			return leds.get(0).schemaSize();
		}

		@Override
		public void close() {
		}

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