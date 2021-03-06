package io.intino.alexandria.led.allocators;

import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.buffers.store.ByteBufferStore;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.util.memory.MemoryAddress;

import java.nio.ByteBuffer;

import static io.intino.alexandria.led.util.memory.MemoryUtils.allocBuffer;

public class DefaultAllocator<T extends Schema> implements SchemaAllocator<T> {

	private final int elementSize;
	private final SchemaFactory<T> factory;

	public DefaultAllocator(int schemaSize, Class<T> schemaClass) {
		this.elementSize = schemaSize;
		this.factory = Schema.factoryOf(schemaClass);
	}

	@Override
	public long size() {
		return Long.MAX_VALUE;
	}

	@Override
	public T malloc() {
		ByteBuffer buffer = allocBuffer(elementSize);
		MemoryAddress address = MemoryAddress.of(buffer);
		ByteStore store = new ByteBufferStore(buffer, address, 0, elementSize);
		return factory.newInstance(store);
	}

	@Override
	public T calloc() {
		T instance = malloc();
		instance.clear();
		return instance;
	}

	@Override
	public int schemaSize() {
		return elementSize;
	}

	@Override
	public void clear() {

	}

	@Override
	public void free() {

	}

	@Override
	public Class<T> schemaClass() {
		return factory.schemaClass();
	}
}
