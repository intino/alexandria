package io.intino.alexandria.led.allocators;

import io.intino.alexandria.led.Transaction;

public interface TransactionAllocator<T extends Transaction> extends AutoCloseable {

	long size();

	T malloc();

	T calloc();

	int transactionSize();

	void clear();

	void free();

	@Override
	default void close() {
		free();
	}
}
