package io.intino.alexandria.led.leds;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocator;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class IndexedLed<T extends Transaction> implements Led<T> {

	private final IndexedAllocator<T> allocator;

	public IndexedLed(IndexedAllocator<T> allocator) {
		this.allocator = requireNonNull(allocator);
	}

	public long size() {
		return allocator.size();
	}

	@Override
	public int transactionSize() {
		return allocator.transactionSize();
	}

	@Override
	public T transaction(int index) {
		if(index >= size()) {
			throw new IndexOutOfBoundsException("Index >= " + size());
		}
		return allocator.malloc(index);
	}
}
