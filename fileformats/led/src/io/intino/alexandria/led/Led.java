package io.intino.alexandria.led;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Led<T extends Schema> extends Iterable<T>, AutoCloseable {
	Stream<T> stream();

	default Stream<T> parallelStream() {
		return stream().parallel();
	}

	default Iterator<T> iterator() {
		return stream().iterator();
	}

	int schemaSize();

	class Filter<T extends Schema> implements Led<T> {
		private final Led<T> led;
		private final Predicate<T> predicate;

		public Filter(Led<T> led, Predicate<T> predicate) {
			this.led = led;
			this.predicate = predicate;
		}

		@Override
		public int schemaSize() {
			return led.schemaSize();
		}

		@Override
		public Stream<T> stream() {
			return led.stream().filter(predicate);
		}

		@Override
		public Stream<T> parallelStream() {
			return led.parallelStream().filter(predicate);
		}

		@Override
		public Iterator<T> iterator() {
			return stream().iterator();
		}

		@Override
		public void close() {
		}

	}

	class Join<T extends Schema> implements Led<T> {
		private final List<Led<T>> leds;

		public Join(List<Led<T>> leds) {
			this.leds = leds;
		}

		@Override
		public Stream<T> stream() {
			return leds.stream().flatMap(this::expand).sorted();
		}

		private Stream<T> expand(Led<T> led) {
			return led.parallelStream();
		}

		@Override
		public Stream<T> parallelStream() {
			return stream().parallel();
		}

		@Override
		public Iterator<T> iterator() {
			return stream().iterator();
		}

		@Override
		public int schemaSize() {
			return leds.get(0).schemaSize();
		}

		@Override
		public void close() {
		}
	}
}