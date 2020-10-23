package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.TransactionAllocator;
import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocator;
import io.intino.alexandria.led.allocators.indexed.ListAllocator;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class LedBuilder<S extends Transaction> {

	private final TransactionAllocator<S> allocator;

	private final int transactionSize;

	public LedBuilder(int transactionSize, TransactionFactory<S> factory) {
		this.transactionSize = transactionSize;
		this.allocator = createBuilderDefaultAllocator(transactionSize, factory);
	}

	public LedBuilder(TransactionAllocator<S> allocator, int transactionSize) {
		this.allocator = requireNonNull(allocator);
		this.transactionSize = transactionSize;
	}

	public S createTransaction() {
		return allocator.malloc();
	}

	public Led<S> build() {
		return new Led<>() {
			@Override
			public long size() {
				return allocator.size();
			}

			@Override
			public int transactionSize() {
				return transactionSize;
			}

			@Override
			public S transaction(int index) {
				if(allocator instanceof IndexedAllocator) {
					return ((IndexedAllocator<S>)allocator).malloc(index);
				}
				throw new UnsupportedOperationException("Allocator is not indexed");
			}

			@Override
			public Iterator<S> iterator() {
				return stream().iterator();
			}

			@Override
			public List<S> elements() {
				return stream().collect(Collectors.toList());
			}

			private Stream<S> stream() {
				if(allocator instanceof IndexedAllocator) {
					final IndexedAllocator<S> theAllocator = (IndexedAllocator<S>) allocator;
					return IntStream.range(0, (int) size()).mapToObj(theAllocator::malloc);
				}
				allocator.clear();
				return IntStream.range(0, (int) size()).mapToObj(i -> allocator.malloc());
			}
		};
	}

	private TransactionAllocator<S> createBuilderDefaultAllocator(int transactionSize, TransactionFactory<S> factory) {
		return new ListAllocator<>(1000, transactionSize, factory);
	}
}
