package io.intino.alexandria.led.util;

import java.nio.ByteBuffer;
import java.util.Objects;

import static io.intino.alexandria.led.util.MemoryUtils.NULL;
import static io.intino.alexandria.led.util.MemoryUtils.addressOf;

public class ModifiableMemoryAddress implements MemoryAddress {
	private long address;

	public ModifiableMemoryAddress(long address) {
		this.address = address;
	}

	@Override
	public long get() {
		return address;
	}

	public void set(long address) {
		this.address = address;
	}

	@Override
	public boolean isNull() {
		return address == NULL;
	}

	@Override
	public boolean notNull() {
		return address != NULL;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MemoryAddress)) return false;
		MemoryAddress that = (MemoryAddress) o;
		return address == that.get();
	}

	@Override
	public int hashCode() {
		return Objects.hash(address);
	}

	public static ModifiableMemoryAddress of(ByteBuffer buffer) {
		return new ModifiableMemoryAddress(addressOf(buffer));
	}
}
