package io.intino.alexandria.led.leds;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.allocators.indexed.ListAllocator;

public class DynamicLed<T extends Schema> implements Led<T> {

	private final ListAllocator<T> allocator;
	private final int schemaSize;

	public DynamicLed(int schemaSize, SchemaFactory<T> factory) {
		this.schemaSize = schemaSize;
		this.allocator = new ListAllocator<>(1000, schemaSize, factory);
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
}
