package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.allocators.indexed.ListAllocator;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LedBuilder<S extends Transaction> {
	private final ListAllocator<S> allocator;
	private final int transactionSize;

	public LedBuilder(int transactionSize, TransactionFactory<S> factory) {
		this(1000, transactionSize, factory);
	}

	public LedBuilder(int bufferSize, int transactionSize, TransactionFactory<S> factory) {
		this.transactionSize = transactionSize;
		this.allocator = new ListAllocator<>(bufferSize, transactionSize, factory);
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
			public Iterator<S> iterator() {
				return elements().iterator();
			}

			@Override
			public List<S> elements() {
				return stream().collect(Collectors.toList());
			}

			private Stream<S> stream() {
				return IntStream.range(0, (int) size()).mapToObj(allocator::malloc);
			}
		};
	}

}
