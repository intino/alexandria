package io.intino.alexandria.led.allocators.stack;

import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.allocators.SchemaFactory;

@FunctionalInterface
public interface StackAllocatorFactory<T extends Schema> {

	StackAllocator<T> create(int elementSize, long elementCount, Class<T> schemaClass);
}
