package io.intino.alexandria.led.allocators.stack;

import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.buffers.store.ByteBufferStore;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.buffers.store.NativePointerStore;
import io.intino.alexandria.led.util.MemoryUtils;
import io.intino.alexandria.led.util.ModifiableMemoryAddress;

import java.nio.ByteBuffer;

import static io.intino.alexandria.led.util.MemoryUtils.allocBuffer;

public final class StackAllocators {

	public static <E extends Transaction> StackAllocator<E> newUnmanaged(int elementSize, long elementCount,
																		 TransactionFactory<E> provider) {

		final long size = elementSize * elementCount;
		final long ptr = MemoryUtils.malloc(size);
		ModifiableMemoryAddress address = new ModifiableMemoryAddress(ptr);
		ByteStore store = new NativePointerStore(address, 0, size);
		return new SingleStackAllocator<>(store, address, elementSize, provider);
	}

	public static <E extends Transaction> StackAllocator<E> newManaged(int elementSize, long elementCount, TransactionFactory<E> provider) {
		if (elementCount < 0) throw new IllegalArgumentException("Element count is negative");
		if (elementCount > Integer.MAX_VALUE)
			throw new IllegalArgumentException("Element Count too large for managed byte store");
		final long size = elementSize * elementCount;
		if (size > Integer.MAX_VALUE) throw new IllegalArgumentException("Size too large for managed byte store");
		final ByteBuffer buffer = allocBuffer((int) size);
		ModifiableMemoryAddress address = ModifiableMemoryAddress.of(buffer);
		ByteStore store = new ByteBufferStore(buffer, address, 0, (int) size);
		return new SingleStackAllocator<>(store, address, elementSize, provider);
	}

	public static <E extends Transaction> StackAllocator<E> newManaged(int elementSize, ByteBuffer buffer,
																	   TransactionFactory<E> provider) {
		ModifiableMemoryAddress address = ModifiableMemoryAddress.of(buffer);
		ByteStore store = new ByteBufferStore(buffer, address, 0, buffer.remaining());
		return new SingleStackAllocator<>(store, address, elementSize, provider);
	}

	private StackAllocators() {
	}
}