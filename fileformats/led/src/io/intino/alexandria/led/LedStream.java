package io.intino.alexandria.led;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public interface LedStream<S extends Transaction> extends Iterator<S>, AutoCloseable {
	int transactionSize();

	class Filter<S extends Transaction> implements LedStream<S> {
		private final LedStream<S> led;
		private final Predicate<S> predicate;

		public Filter(LedStream<S> ledStream, Predicate<S> predicate) {
			this.led = ledStream;
			this.predicate = predicate;
		}

		@Override
		public int transactionSize() {
			return led.transactionSize();
		}

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public S next() {
			return null;
		}

		@Override
		public void close() {
		}
	}

	class Join<S extends Transaction> implements LedStream<S> {
		private final List<LedStream<S>> leds;

		public Join(List<LedStream<S>> leds) {
			this.leds = leds;
		}

		@Override
		public int transactionSize() {
			return leds.get(0).transactionSize();
		}

		@Override
		public void close() {
		}

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public S next() {
			return null;
		}
	}
}