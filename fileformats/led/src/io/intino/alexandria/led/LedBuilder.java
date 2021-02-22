package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.indexed.IndexedAllocator;
import io.intino.alexandria.led.allocators.indexed.ListAllocator;
import io.intino.alexandria.led.leds.ListLed;
import io.intino.alexandria.led.util.memory.LedLibraryConfig;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * Consider using {@link HeapLedStreamBuilder} first when performance is critical.
 *
 * */
public class LedBuilder<T extends Schema> implements Led.Builder<T> {

	public static final int DEFAULT_INITIAL_CAPACITY = 10_000;
	public static final float GROW_FACTOR = 1.5f;


	private final Class<T> schemaClass;
	private final IndexedAllocator<T> allocator;
	private T[] sortedTransactions;
	private int size;

	public LedBuilder(Class<T> schemaClass) {
		this(schemaClass, createBuilderDefaultAllocator(Schema.sizeOf(schemaClass), schemaClass));
	}

	@SuppressWarnings("unchecked")
	public LedBuilder(Class<T> schemaClass, IndexedAllocator<T> allocator) {
		this.schemaClass = requireNonNull(schemaClass);
		this.allocator = requireNonNull(allocator);
		sortedTransactions = (T[]) new Schema[DEFAULT_INITIAL_CAPACITY];
	}

	@Override
	public Class<T> schemaClass() {
		return schemaClass;
	}

	@Override
	public int schemaSize() {
		return allocator.schemaSize();
	}

	@Override
	public Led.Builder<T> create(Consumer<T> initializer) {
		T schema = allocator.malloc();
		initializer.accept(schema);
		putInSortedList(schema);
		return this;
	}

	private void putInSortedList(T schema) {
		if(size == sortedTransactions.length) {
			grow();
		} else if(size == 0) {
			sortedTransactions[0] = schema;
		} else {
			int index = Arrays.binarySearch(sortedTransactions, 0, size, schema);
			if(index < 0) {
				index = -index + 1;
			}
			System.arraycopy(sortedTransactions, index, sortedTransactions, index + 1, ++size - index);
			sortedTransactions[index] = schema;
		}
	}

	private void grow() {
		sortedTransactions = Arrays.copyOf(sortedTransactions, Math.round(size * GROW_FACTOR));
	}

	public Led<T> build() {
		return new ListLed<>(schemaClass, getList());
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

	private static <T extends Schema> IndexedAllocator<T> createBuilderDefaultAllocator(int schemaSize, Class<T> schemaClass) {
		return new ListAllocator<>(DEFAULT_INITIAL_CAPACITY, schemaSize, schemaClass);
	}
}
