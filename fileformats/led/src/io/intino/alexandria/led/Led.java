package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocator;
import io.intino.alexandria.led.leds.IteratorLedStream;
import io.intino.alexandria.led.leds.ListLed;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public interface Led<T extends Transaction> extends Iterable<T> {

	static <T extends Transaction> Led<T> empty() {
		return new ListLed<>(Collections.emptyList());
	}

	static <T extends Transaction> Led<T> fromLedStream(LedStream<T> ledStream) {
		return new ListLed<>(ledStream.asJavaStream().collect(Collectors.toUnmodifiableList()));
	}

	static <T extends Transaction> Builder<T> builder(Class<T> transactionClass) {
		return new LedBuilder<>(transactionClass);
	}

	static <T extends Transaction> Builder<T> builder(Class<T> transactionClass, TransactionFactory<T> factory) {
		return new LedBuilder<>(transactionClass, factory);
	}

	static <T extends Transaction> Builder<T> builder(Class<T> transactionClass, IndexedAllocator<T> allocator) {
		return new LedBuilder<>(transactionClass, allocator);

	}

	long size();

	int transactionSize();

	T transaction(int index);

	@Override
	default Iterator<T> iterator() {
		return elements().iterator();
	}

	default List<T> elements() {
		return new AbstractList<T>() {
			@Override
			public T get(int index) {
				return transaction(index);
			}

			@Override
			public int size() {
				return (int) Led.this.size();
			}
		};
	}

	default LedStream<T> toLedStream() {
		return new IteratorLedStream<>(transactionSize(), iterator());
	}

	interface Builder<T extends Transaction> {
		Class<T> transactionClass();

		int transactionSize();

		Builder<T> create(Consumer<T> initializer);

		Led<T> build();
	}
}