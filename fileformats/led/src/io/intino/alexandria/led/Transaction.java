package io.intino.alexandria.led;

import io.intino.alexandria.led.buffers.BigEndianBitBuffer;
import io.intino.alexandria.led.buffers.BitBuffer;
import io.intino.alexandria.led.buffers.LittleEndianBitBuffer;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.util.OffHeapObject;

import java.nio.ByteOrder;
import java.util.Objects;

public abstract class Transaction implements OffHeapObject, Comparable<Transaction> {

	protected final BitBuffer bitBuffer;

	public Transaction(ByteStore store) {
		bitBuffer = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ?
				new LittleEndianBitBuffer(store) :
				new BigEndianBitBuffer(store);
	}

	public abstract long id();

	public abstract int size();

	@Override
	public int hashCode() {
		return Objects.hashCode(id());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj.getClass() != getClass()) return false;
		Transaction other = (Transaction) obj;
		return notNull() && other.notNull() && id() == other.id();
	}

	public boolean isReadOnly() {
		return bitBuffer.isReadOnly();
	}

	@Override
	public boolean isNull() {
		return bitBuffer.isNull();
	}

	@Override
	public boolean notNull() {
		return bitBuffer.notNull();
	}

	@Override
	public int compareTo(Transaction o) {
		return Long.compare(id(), o.id());
	}

	public void clear() {
		bitBuffer.clear();
	}

	@Override
	public long address() {
		return bitBuffer.address();
	}

	@Override
	public long byteSize() {
		return bitBuffer.byteSize();
	}

	@Override
	public long baseOffset() {
		return bitBuffer.baseOffset();
	}

	public void invalidate() {
		bitBuffer.invalidate();
	}

	public long bitCount() {
		return bitBuffer.bitCount();
	}

	public long endOffset() {
		return bitBuffer.endOffset();
	}

	public String toBinaryString() {
		return bitBuffer.toBinaryString();
	}

	public String toBinaryString(int splitSize) {
		return bitBuffer.toBinaryString(splitSize);
	}

	public String toHexString() {
		return bitBuffer.toHexString();
	}
}