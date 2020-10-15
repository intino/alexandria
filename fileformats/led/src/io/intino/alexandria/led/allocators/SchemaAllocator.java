package io.intino.alexandria.led.allocators;

import io.intino.alexandria.led.Schema;

public interface SchemaAllocator<T extends Schema> extends AutoCloseable {

	T malloc();

	T calloc();

	int elementSize();

	void clear();

	void free();

	@Override
	default void close() {
		free();
	}
}
