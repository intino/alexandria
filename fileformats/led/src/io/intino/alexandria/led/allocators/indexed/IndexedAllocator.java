package io.intino.alexandria.led.allocators.indexed;

import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.allocators.SchemaAllocator;

public interface IndexedAllocator<T extends Transaction> extends SchemaAllocator<T> {

	T malloc(int index);

	T calloc(int index);

	void clear(int index);

	long byteSize();

	int size();
}
