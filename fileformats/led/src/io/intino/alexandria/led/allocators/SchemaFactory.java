package io.intino.alexandria.led.allocators;

import io.intino.alexandria.led.buffers.store.ByteStore;

public interface SchemaFactory<T> {
	T newInstance(ByteStore store);
}
