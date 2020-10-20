package io.intino.alexandria.led.allocators.stack;

import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.allocators.TransactionFactory;

@FunctionalInterface
public interface StackAllocatorFactory<T extends Transaction> {

	StackAllocator<T> create(int elementSize, long elementCount, TransactionFactory<T> schemaFactory);
}
