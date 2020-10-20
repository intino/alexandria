package io.intino.alexandria.led.leds;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocator;
import io.intino.alexandria.led.allocators.indexed.ManagedIndexedAllocator;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ManagedIndexedLed<S extends Transaction> implements Led<S> {
	private final IndexedAllocator<S> allocator;

	public ManagedIndexedLed(ByteBuffer buffer, int baseOffset, int size, int transactionSize, TransactionFactory<S> factory) {
		allocator = new ManagedIndexedAllocator<>(buffer, baseOffset, size, transactionSize, factory);
	}

	public long size() {
		return allocator.size();
	}

	@Override
	public int transactionSize() {
		return allocator.transactionSize();
	}

	@Override
	public Iterator<S> iterator() {
		return elements().listIterator();
	}

	@Override
	public List<S> elements() {
		return stream().collect(Collectors.toList());
	}

	private Stream<S> stream() {
		return IntStream.range(0, (int) size()).mapToObj(allocator::malloc);
	}

	private long byteSize() {
		return allocator.byteSize();
	}
}
