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

/**
 * Temporalmente obsoleta. Use {@link LedStreamBuilder}.
 *
 * */
@Deprecated
public class LedBuilder<T extends Transaction> {

	private final TransactionAllocator<T> allocator;

	private final int transactionSize;

	public LedBuilder(int transactionSize, TransactionFactory<T> factory) {
		this.transactionSize = transactionSize;
		this.allocator = createBuilderDefaultAllocator(transactionSize, factory);
	}

	public LedBuilder(TransactionAllocator<T> allocator, int transactionSize) {
		this.allocator = requireNonNull(allocator);
		this.transactionSize = transactionSize;
	}

	public T createTransaction() {
		return allocator.malloc();
	}

	public Led<T> build() {
		return null;
	}

	private TransactionAllocator<T> createBuilderDefaultAllocator(int transactionSize, TransactionFactory<T> factory) {
		return new ListAllocator<>(1000, transactionSize, factory);
	}
}
