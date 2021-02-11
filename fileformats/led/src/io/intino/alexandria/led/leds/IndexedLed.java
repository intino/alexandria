package io.intino.alexandria.led.leds;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocator;

import static java.util.Objects.requireNonNull;

public class IndexedLed<T extends Schema> implements Led<T> {

	private final IndexedAllocator<T> allocator;

	public IndexedLed(IndexedAllocator<T> allocator) {
		this.allocator = requireNonNull(allocator);
	}

	public long size() {
		return allocator.size();
	}

	@Override
	public int schemaSize() {
		return allocator.schemaSize();
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
