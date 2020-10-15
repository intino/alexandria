package io.intino.alexandria.led;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Led<T extends Schema> extends Iterable<T>, AutoCloseable {

	int elementSize();

	Stream<T> stream();

	default Stream<T> parallelStream() {
		return stream().parallel();
	}

	default Iterator<T> iterator() {
		return stream().iterator();
	}

	class Filter<T extends Schema> implements Led<T> {
		private final Led<T> led;
		private final Predicate<T> predicate;

		public Filter(Led<T> led, Predicate<T> predicate) {
			this.led = led;
			this.predicate = predicate;
		}

		@Override
		public int elementSize() {
			return led.elementSize();
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
		public void close() throws Exception {

		}

	}

	class Join<T extends Schema> implements Led<T> {
		private final int elementSize;
		private final List<Led<T>> ledgers;

		public Join(int elementSize, List<Led<T>> ledgers) {
			this.elementSize = elementSize;
			this.ledgers = ledgers;
		}

		@Override
		public int elementSize() {
			return elementSize;
		}

		@Override
		public Stream<T> stream() {
			return ledgers.stream().flatMap(this::expand).sorted();
		}

		private Stream<T> expand(Led<T> ledger) {
			return ledger.parallelStream();
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
		public void close() {
		}
	}
}