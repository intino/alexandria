package io.intino.alexandria.led.util;

import java.nio.ByteBuffer;

import static io.intino.alexandria.led.util.MemoryUtils.addressOf;


public interface MemoryAddress {

	static MemoryAddress of(ByteBuffer buffer) {
		return new MemoryAddress() {
			private final long address = addressOf(buffer);

			@Override
			public long get() {
				return address;
			}
		};
	}

	long get();

	default boolean isNull() {
		return get() == MemoryUtils.NULL;
	}

	default boolean notNull() {
		return get() != MemoryUtils.NULL;
	}
}
