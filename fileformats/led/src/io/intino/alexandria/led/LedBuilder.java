package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocator;
import io.intino.alexandria.led.allocators.indexed.ListAllocator;
import io.intino.alexandria.led.leds.ListLed;

import java.util.*;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * Consider using {@link HeapLedStreamBuilder} first when performance is critical.
 *
 * */
public class LedBuilder<T extends Transaction> implements Led.Builder<T> {

	public static final int DEFAULT_INITIAL_CAPACITY = 1024;
	public static final float GROW_FACTOR = 1.5f;


	private final Class<T> transactionClass;
	private final IndexedAllocator<T> allocator;
	private T[] sortedTransactions;
	private int size;

	public LedBuilder(Class<T> transactionClass) {
		this(transactionClass, Transaction.factoryOf(transactionClass));
	}

	public LedBuilder(Class<T> transactionClass, TransactionFactory<T> factory) {
		this(transactionClass, createBuilderDefaultAllocator(Transaction.sizeOf(transactionClass), factory));
	}

	@SuppressWarnings("unchecked")
	public LedBuilder(Class<T> transactionClass, IndexedAllocator<T> allocator) {
		this.transactionClass = requireNonNull(transactionClass);
		this.allocator = requireNonNull(allocator);
		sortedTransactions = (T[]) new Transaction[DEFAULT_INITIAL_CAPACITY];
	}

	@Override
	public Class<T> transactionClass() {
		return transactionClass;
	}

	@Override
	public int transactionSize() {
		return allocator.transactionSize();
	}

	@Override
	public Led.Builder<T> create(Consumer<T> initializer) {
		T transaction = allocator.malloc();
		initializer.accept(transaction);
		putInSortedList(transaction);
		return this;
	}

	private void putInSortedList(T transaction) {
		if(size == sortedTransactions.length) {
			grow();
		} else if(size == 0) {
			sortedTransactions[0] = transaction;
		} else {
			int index = Arrays.binarySearch(sortedTransactions, 0, size, transaction);
			if(index < 0) {
				index = -index + 1;
			}
			System.arraycopy(sortedTransactions, index, sortedTransactions, index + 1, ++size - index);
			sortedTransactions[index] = transaction;
		}
	}

	private void grow() {
		sortedTransactions = Arrays.copyOf(sortedTransactions, Math.round(size * GROW_FACTOR));
	}

	public Led<T> build() {
		return new ListLed<>(getList());
	}

	private List<T> getList() {
		return new AbstractList<>() {
			@Override
			public T get(int index) {
				if(index >= size) {
					throw new IndexOutOfBoundsException(index + " >= " + size);
				}
				return sortedTransactions[index];
			}

			@Override
			public int size() {
				return size;
			}
		};
	}

	private static <T extends Transaction> IndexedAllocator<T> createBuilderDefaultAllocator(int transactionSize, TransactionFactory<T> factory) {
		return new ListAllocator<>(DEFAULT_INITIAL_CAPACITY, transactionSize, factory);
	}
}
