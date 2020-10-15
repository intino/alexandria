package io.intino.alexandria.led.allocators.indexed;

import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.buffers.store.ByteBufferStore;
import io.intino.alexandria.led.util.MemoryUtils;
import io.intino.alexandria.led.util.ModifiableMemoryAddress;

import java.nio.ByteBuffer;

import static io.intino.alexandria.led.util.MemoryUtils.*;

public class ManagedIndexedAllocator<T extends Schema> implements IndexedAllocator<T> {

	private ByteBufferStore store;
	private final ModifiableMemoryAddress address;
	private final int elementSize;
	private final SchemaFactory<T> factory;

	public ManagedIndexedAllocator(ByteBuffer buffer, int baseOffset, int size, int elementSize, SchemaFactory<T> factory) {
		this.elementSize = elementSize;
		this.factory = factory;
		address = ModifiableMemoryAddress.of(buffer);
		store = new ByteBufferStore(buffer, address, baseOffset, size);
	}

	public ManagedIndexedAllocator(long elementsCount, int elementSize, SchemaFactory<T> factory) {
		this.elementSize = elementSize;
		this.factory = factory;
		final long size = elementsCount * elementSize;
		if (size > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Size too large for ManagedIndexedAllocator");
		}
		ByteBuffer buffer = allocBuffer((int) size);
		address = ModifiableMemoryAddress.of(buffer);
		store = new ByteBufferStore(buffer, address, 0, (int) size);
	}

	@Override
	public T malloc(int index) {
		final int offset = index * elementSize;
		return factory.newInstance(store.slice(offset, elementSize));
	}

	@Override
	public T calloc(int index) {
		T instance = malloc(index);
		instance.clear();
		return instance;
	}

	@Override
	public void clear(int index) {
		memset(address.get() + index * elementSize, elementSize, 0);
	}

	@Override
	public long byteSize() {
		return store.byteSize();
	}

	@Override
	public int count() {
		return (int) (byteSize() / elementSize);
	}

	@Override
	public T malloc() {
		return malloc(0);
	}

	@Override
	public T calloc() {
		return calloc(0);
	}

	@Override
	public int elementSize() {
		return elementSize;
	}

	@Override
	public void clear() {
		store.clear();
	}

	@Override
	public void free() {
		if (address.get() != NULL) {
			MemoryUtils.free(store.storeImpl());
			store = null;
			address.set(NULL);
		}
	}
}
