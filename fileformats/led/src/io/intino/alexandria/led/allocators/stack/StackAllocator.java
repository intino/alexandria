package io.intino.alexandria.led.allocators.stack;

import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.allocators.TransactionAllocator;

public interface StackAllocator<T extends Transaction> extends TransactionAllocator<T> {

	long stackPointer();

	long address();

	long stackSize();

	long remainingBytes();

	T malloc();

	T calloc();

	void pop();

	void clear();
}
