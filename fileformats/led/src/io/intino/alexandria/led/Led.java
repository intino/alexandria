package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocator;
import io.intino.alexandria.led.leds.IteratorLedStream;
import io.intino.alexandria.led.leds.ListLed;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public interface Led<T extends Schema> extends Iterable<T> {

	static <T extends Schema> Led<T> empty() {
		return new ListLed<>(Collections.emptyList());
	}

	static <T extends Schema> Led<T> fromLedStream(LedStream<T> ledStream) {
		return new ListLed<>(ledStream.asJavaStream().collect(Collectors.toUnmodifiableList()));
	}

	static <T extends Schema> Builder<T> builder(Class<T> schemaClass) {
		return new LedBuilder<>(schemaClass);
	}

	static <T extends Schema> Builder<T> builder(Class<T> schemaClass, SchemaFactory<T> factory) {
		return new LedBuilder<>(schemaClass, factory);
	}

	static <T extends Schema> Builder<T> builder(Class<T> schemaClass, IndexedAllocator<T> allocator) {
		return new LedBuilder<>(schemaClass, allocator);

	}

	long size();

	int schemaSize();

	T schema(int index);

	@Override
	default Iterator<T> iterator() {
		return elements().iterator();
	}

	default List<T> elements() {
		return new AbstractList<T>() {
			@Override
			public T get(int index) {
				return schema(index);
			}

			@Override
			public int size() {
				return (int) Led.this.size();
			}
		};
	}

	default LedStream<T> toLedStream() {
		return new IteratorLedStream<>(schemaSize(), iterator());
	}

	interface Builder<T extends Schema> {
		Class<T> schemaClass();

		int schemaSize();

		Builder<T> create(Consumer<T> initializer);

		Led<T> build();
	}
}