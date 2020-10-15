package io.intino.alexandria.led;


import io.intino.alexandria.led.buffers.BigEndianBitBuffer;
import io.intino.alexandria.led.buffers.BitBuffer;
import io.intino.alexandria.led.buffers.LittleEndianBitBuffer;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.util.OffHeapObject;

import java.nio.ByteOrder;
import java.util.Objects;


public abstract class Schema implements OffHeapObject, Comparable<Schema> {
	private final BitBuffer bitBuffer;

	public Schema(ByteStore store) {
		if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
			bitBuffer = new LittleEndianBitBuffer(store);
		} else {
			bitBuffer = new BigEndianBitBuffer(store);
		}
	}

	public abstract long id();

	@Override
	public int hashCode() {
		return Objects.hashCode(id());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Schema other = (Schema) obj;
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
	public int compareTo(Schema o) {
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

	protected long getInteger(int bitIndex, int bitCount) {
		return bitBuffer.getInteger(bitIndex, bitCount);
	}

	protected float getFloat(int bitIndex) {
		return bitBuffer.getFloat(bitIndex);
	}

	protected double getDouble(int bitIndex) {
		return bitBuffer.getDouble(bitIndex);
	}

	protected void setInteger(int bitIndex, int bitCount, long value) {
		bitBuffer.setInteger(bitIndex, bitCount, value);
	}

	protected void setInt8(int byteIndex, int bitOffset, int bitCount, long value) {
		bitBuffer.setInt8(byteIndex, bitOffset, bitCount, value);
	}

	protected void setInt16(int byteIndex, int bitOffset, int bitCount, long value) {
		bitBuffer.setInt16(byteIndex, bitOffset, bitCount, value);
	}

	protected void setInt32(int byteIndex, int bitOffset, int bitCount, long value) {
		bitBuffer.setInt32(byteIndex, bitOffset, bitCount, value);
	}

	protected void setInt64(int byteIndex, int bitOffset, int bitCount, long value) {
		bitBuffer.setInt64(byteIndex, bitOffset, bitCount, value);
	}

	protected void setFloat(int bitIndex, float value) {
		bitBuffer.setFloat(bitIndex, value);
	}

	protected void setDouble(int bitIndex, double value) {
		bitBuffer.setDouble(bitIndex, value);
	}

	protected byte getAlignedByte(int byteIndex) {
		return bitBuffer.getAlignedByte(byteIndex);
	}

	protected short getAlignedShort(int byteIndex) {
		return bitBuffer.getAlignedShort(byteIndex);
	}

	protected char getAlignedChar(int byteIndex) {
		return getAlignedChar(byteIndex);
	}

	protected int getAlignedInt(int byteIndex) {
		return bitBuffer.getAlignedInt(byteIndex);
	}

	protected long getAlignedLong(int byteIndex) {
		return bitBuffer.getAlignedLong(byteIndex);
	}

	protected float getAlignedFloat(int byteIndex) {
		return bitBuffer.getAlignedFloat(byteIndex);
	}

	protected double getAlignedDouble(int byteIndex) {
		return bitBuffer.getAlignedDouble(byteIndex);
	}

	protected void setAlignedByte(int byteIndex, byte value) {
		bitBuffer.setAlignedByte(byteIndex, value);
	}

	protected void setAlignedShort(int byteIndex, short value) {
		bitBuffer.setAlignedShort(byteIndex, value);
	}

	protected void setAlignedChar(int byteIndex, char value) {
		bitBuffer.setAlignedChar(byteIndex, value);
	}

	protected void setAlignedInt(int byteIndex, int value) {
		bitBuffer.setAlignedInt(byteIndex, value);
	}

	protected void setAlignedLong(int byteIndex, long value) {
		bitBuffer.setAlignedLong(byteIndex, value);
	}

	protected void setAlignedFloat(int byteIndex, float value) {
		bitBuffer.setAlignedFloat(byteIndex, value);
	}

	protected void setAlignedDouble(int byteIndex, double value) {
		bitBuffer.setAlignedDouble(byteIndex, value);
	}
}
