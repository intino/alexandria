package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocator;
import io.intino.alexandria.led.allocators.indexed.ManagedIndexedAllocator;
import io.intino.alexandria.led.allocators.indexed.UnmanagedIndexedAllocator;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ArrayLed<T extends Schema> implements IndexedLed<T> {
	private final IndexedAllocator<T> allocator;

	public ArrayLed(long baseAddress, long baseOffset, long size, int elementSize, SchemaFactory<T> factory) {
		allocator = new UnmanagedIndexedAllocator<>(baseAddress, baseOffset, size, elementSize, factory);
	}

	public ArrayLed(ByteBuffer buffer, int baseOffset, int size, int elementSize, SchemaFactory<T> factory) {
		allocator = new ManagedIndexedAllocator<>(buffer, baseOffset, size, elementSize, factory);
	}

	public ArrayLed(IndexedAllocator<T> allocator) {
		this.allocator = allocator;
	}

	@Override
	public long size() {
		return allocator.size();
	}

	@Override
	public int schemaSize() {
		return allocator.elementSize();
	}

	@Override
	public T get(int index) {
		return allocator.malloc(index);
	}

	@Override
	public List<T> getAll() {
		return stream().collect(Collectors.toList());
	}

	@Override
	public Stream<T> stream() {
		return IntStream.range(0, (int) size()).mapToObj(this::get);
	}

	public long byteSize() {
		return allocator.byteSize();
	}

	@Override
	public void close() throws Exception {
		allocator.free();
	}
}
