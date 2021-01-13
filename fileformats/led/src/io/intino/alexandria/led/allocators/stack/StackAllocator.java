package io.intino.alexandria.led.allocators.stack;

import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.allocators.SchemaAllocator;

public interface StackAllocator<T extends Schema> extends SchemaAllocator<T> {

	long stackPointer();

	long address();

	long stackSize();

	long remainingBytes();

	T malloc();

	T calloc();

	void pop();

	void clear();
}
