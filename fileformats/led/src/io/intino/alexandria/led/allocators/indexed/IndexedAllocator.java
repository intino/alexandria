package io.intino.alexandria.led.allocators.indexed;

import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.allocators.SchemaAllocator;

public interface IndexedAllocator<T extends Schema> extends SchemaAllocator<T> {

	T malloc(int index);

	T calloc(int index);

	void clear(int index);

	long byteSize();

	int count();
}
