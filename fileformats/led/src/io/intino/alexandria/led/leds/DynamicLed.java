package io.intino.alexandria.led.leds;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.allocators.indexed.ListAllocator;
import io.intino.alexandria.led.util.memory.LedLibraryConfig;

public class DynamicLed<T extends Schema> implements Led<T> {

	private final ListAllocator<T> allocator;
	private final int schemaSize;

	public DynamicLed(Class<T> schemaClass) {
		this.schemaSize = Schema.sizeOf(schemaClass);
		this.allocator = new ListAllocator<>(LedLibraryConfig.DEFAULT_BUFFER_SIZE.get(), schemaSize, schemaClass);
	}

	public Schema newTransaction() {
		return allocator.malloc();
	}

	@Override
	public long size() {
		return allocator.size();
	}

	@Override
	public int schemaSize() {
		return schemaSize;
	}

	@Override
	public T schema(int index) {
		if(index >= size()) {
			throw new IndexOutOfBoundsException("Index >= " + size());
		}
		return allocator.malloc(index);
	}

	@Override
	public Class<T> schemaClass() {
		return allocator.schemaClass();
	}
}
