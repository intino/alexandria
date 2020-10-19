package io.intino.alexandria.led.allocators.indexed;

import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.buffers.store.NativePointerStore;
import io.intino.alexandria.led.util.MemoryUtils;
import io.intino.alexandria.led.util.ModifiableMemoryAddress;

import static io.intino.alexandria.led.util.MemoryUtils.NULL;
import static io.intino.alexandria.led.util.MemoryUtils.memset;

public class UnmanagedIndexedAllocator<T extends Schema> implements IndexedAllocator<T> {

	private NativePointerStore store;
	private final ModifiableMemoryAddress address;
	private final int elementSize;
	private final SchemaFactory<T> factory;

	public UnmanagedIndexedAllocator(long baseAddress, long baseOffset, long size, int elementSize, SchemaFactory<T> factory) {
		this.elementSize = elementSize;
		this.factory = factory;
		this.address = new ModifiableMemoryAddress(baseAddress);
		store = new NativePointerStore(address, baseOffset, size);
	}

	public UnmanagedIndexedAllocator(long elementsCount, int elementSize, SchemaFactory<T> factory) {
		this.elementSize = elementSize;
		this.factory = factory;
		final long size = elementsCount * elementSize;
		address = new ModifiableMemoryAddress(MemoryUtils.malloc(size));
		store = new NativePointerStore(address, 0, size);
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
	public int size() {
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
	public int schemaSize() {
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
