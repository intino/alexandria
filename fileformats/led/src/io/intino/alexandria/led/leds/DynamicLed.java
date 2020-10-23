package io.intino.alexandria.led.leds;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.allocators.indexed.ListAllocator;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DynamicLed <S extends Transaction> implements Led<S> {

	private final ListAllocator<S> allocator;
	private final int transactionSize;

	public DynamicLed(int schemaSize, TransactionFactory<S> factory) {
		this.transactionSize = schemaSize;
		this.allocator = new ListAllocator<>(1000, schemaSize, factory);
	}

	public Transaction newTransaction() {
		return allocator.malloc();
	}

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
		if(index >= size()) {
			throw new IndexOutOfBoundsException("Index >= " + size());
		}
		return allocator.malloc(index);
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
}
