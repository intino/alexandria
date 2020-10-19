package io.intino.alexandria.led.leds;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocator;
import io.intino.alexandria.led.allocators.indexed.ManagedIndexedAllocator;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ManagedIndexedLed<S extends Schema> implements Led<S> {
	private final IndexedAllocator<S> allocator;

	public ManagedIndexedLed(ByteBuffer buffer, int baseOffset, int size, int schemaSize, SchemaFactory<S> factory) {
		allocator = new ManagedIndexedAllocator<>(buffer, baseOffset, size, schemaSize, factory);
	}

	public long size() {
		return allocator.size();
	}

	@Override
	public int schemaSize() {
		return allocator.schemaSize();
	}

	@Override
	public Iterator<S> iterator() {
		return elements().listIterator();
	}

	@Override
	public List<S> elements() {
		return stream().collect(Collectors.toList());
	}

	private Stream<S> stream() {
		return IntStream.range(0, (int) size()).mapToObj(allocator::malloc);
	}

	private long byteSize() {
		return allocator.byteSize();
	}
}
