package io.intino.alexandria.led.leds;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.LedStream;
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

import static java.util.Objects.requireNonNull;

public class IndexedLed<S extends Transaction> implements Led<S> {

	private final IndexedAllocator<S> allocator;

	public IndexedLed(IndexedAllocator<S> allocator) {
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
	public S transaction(int index) {
		if(index >= size()) {
			throw new IndexOutOfBoundsException("Index >= " + size());
		}
		return allocator.malloc(index);
	}

	@Override
	public Iterator<S> iterator() {
		return stream2().iterator();
	}

	@Override
	public List<S> elements() {
		return stream2().collect(Collectors.toList());
	}

	private Stream<S> stream2() {
		return IntStream.range(0, (int) size()).mapToObj(allocator::malloc);
	}

	@Override
	public LedStream<S> stream() {
		return null;
	}
}
