package io.intino.alexandria.led.allocators.indexed;

import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.buffers.store.NativePointerStore;
import io.intino.alexandria.led.util.MemoryUtils;
import io.intino.alexandria.led.util.ModifiableMemoryAddress;
import io.intino.alexandria.led.util.NativePointerCleaner;

import java.lang.ref.Cleaner;

import static io.intino.alexandria.led.util.MemoryUtils.*;

public class UnmanagedIndexedAllocator<T extends Transaction> implements IndexedAllocator<T> {

	private static final Cleaner CLEANER = Cleaner.create();

	private NativePointerStore store;
	private final ModifiableMemoryAddress address;
	private final int elementSize;
	private final TransactionFactory<T> factory;
	private final Cleaner.Cleanable cleanable;

	public UnmanagedIndexedAllocator(long baseAddress, long baseOffset, long size, int elementSize, TransactionFactory<T> factory) {
		this.elementSize = elementSize;
		this.factory = factory;
		this.address = new ModifiableMemoryAddress(baseAddress);
		store = new NativePointerStore(address, baseOffset, size);
		cleanable = CLEANER.register(this, new NativePointerCleaner(address));
	}

	public UnmanagedIndexedAllocator(long elementsCount, int elementSize, TransactionFactory<T> factory) {
		this.elementSize = elementSize;
		this.factory = factory;
		final long size = elementsCount * elementSize;
		address = new ModifiableMemoryAddress(MemoryUtils.malloc(size));
		store = new NativePointerStore(address, 0, size);
		cleanable = CLEANER.register(this, new NativePointerCleaner(address));
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
	public long size() {
		return (byteSize() / elementSize);
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
	public int transactionSize() {
		return elementSize;
	}

	@Override
	public void clear() {
		// store.clear();
	}

	@Override
	public void free() {
		if (address.get() != NULL) {
			cleanable.clean();
			store = null;
		}
	}

}
