package io.intino.alexandria.led.buffers;

import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.buffers.store.ReadOnlyByteStore;
import io.intino.alexandria.led.util.BitUtils;

import static io.intino.alexandria.led.util.BitUtils.write;
import static java.util.Objects.requireNonNull;

public abstract class AbstractBitBuffer implements BitBuffer {

	private ByteStore store;

	public AbstractBitBuffer(ByteStore store) {
		this.store = requireNonNull(store);
	}

	@Override
	public final long address() {
		return store.address();
	}

	@Override
	public void invalidate() {
		if (!isReadOnly()) {
			store = new ReadOnlyByteStore(store);
		}
	}

	@Override
	public long byteSize() {
		return store.byteSize();
	}

	@Override
	public final long bitCount() {
		return store.bitCount();
	}

	@Override
	public long baseOffset() {
		return store.baseOffset();
	}

	@Override
	public long endOffset() {
		return baseOffset() + byteSize();
	}

	protected long getInteger(int bitCount, int bitOffset, int byteIndex, int numBytes) {
		long value;
		switch (numBytes) {
			case Byte.BYTES:
				value = getAlignedByte(byteIndex);
				value &= 0xFF;
				break;
			case Short.BYTES:
				value = getAlignedShort(byteIndex);
				value &= 0XFFFF;
				break;
			case Integer.BYTES:
				value = getAlignedInt(byteIndex);
				value &= 0xFFFFFFFF;
				break;
			case Long.BYTES:
				value = getAlignedLong(byteIndex);
				break;
			default:
				throw new IllegalArgumentException("Unsupported number of bits " + bitCount);
		}
		return BitUtils.read(value, bitOffset, bitCount);
	}

	@Override
	public float getFloat(int bitIndex) {
		//final int byteIndex = byteIndex(bitIndex);
		//final int bits = store.getInt(byteIndex);
		//return Float.intBitsToFloat(bits);
		throw new UnsupportedOperationException();
	}

	@Override
	public double getDouble(int bitIndex) {
		//final int byteIndex = byteIndex(bitIndex);
		//final long bits = store.getLong(byteIndex);
		//return Double.longBitsToDouble(bits);
		throw new UnsupportedOperationException();
	}

	protected void setInteger(int bitCount, long value, int byteIndex, int numBytes, int bitOffset) {
		switch (numBytes) {
			case Byte.BYTES:
				setInt8(byteIndex, bitOffset, bitCount, value);
				break;
			case Short.BYTES:
				setInt16(byteIndex, bitOffset, bitCount, value);
				break;
			case Integer.BYTES:
				setInt32(byteIndex, bitOffset, bitCount, value);
				break;
			case Long.BYTES:
				setInt64(byteIndex, bitOffset, bitCount, value);
				break;
			default:
				throw new IllegalArgumentException("Unsupported number of bits " + bitCount);
		}
	}

	@Override
	public void setInt8(int byteIndex, int bitOffset, int bitCount, long value) {
		final byte oldValue = getAlignedByte(byteIndex);
		final byte newValue = (byte) (write(oldValue, (byte) value, bitOffset, bitCount));
		store.setByte(byteIndex, newValue);
	}

	@Override
	public void setInt16(int byteIndex, int bitOffset, int bitCount, long value) {
		final short oldValue = getAlignedShort(byteIndex);
		final short newValue = (short) (write(oldValue, (short) value, bitOffset, bitCount));
		store.setShort(byteIndex, newValue);
	}

	@Override
	public void setInt32(int byteIndex, int bitOffset, int bitCount, long value) {
		final int oldValue = getAlignedInt(byteIndex);
		final int newValue = (int) (write(oldValue, (int) value, bitOffset, bitCount));
		store.setInt(byteIndex, newValue);
	}

	@Override
	public void setInt64(int byteIndex, int bitOffset, int bitCount, long value) {
		final long oldValue = getAlignedLong(byteIndex);
		final long newValue = write(oldValue, value, bitOffset, bitCount);
		store.setLong(byteIndex, newValue);
	}

	@Override
	public void setFloat(int bitIndex, float value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDouble(int bitIndex, double value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte getAlignedByte(int byteIndex) {
		return store.getByte(byteIndex);
	}

	@Override
	public short getAlignedShort(int byteIndex) {
		return store.getShort(byteIndex);
	}

	@Override
	public char getAlignedChar(int byteIndex) {
		return store.getChar(byteIndex);
	}

	@Override
	public int getAlignedInt(int byteIndex) {
		return store.getInt(byteIndex);
	}

	@Override
	public long getAlignedLong(int byteIndex) {
		return store.getLong(byteIndex);
	}

	@Override
	public float getAlignedFloat(int byteIndex) {
		return store.getFloat(byteIndex);
	}

	@Override
	public double getAlignedDouble(int byteIndex) {
		return store.getDouble(byteIndex);
	}

	@Override
	public void setAlignedByte(int byteIndex, byte value) {
		store.setByte(byteIndex, value);
	}

	@Override
	public void setAlignedShort(int byteIndex, short value) {
		store.setShort(byteIndex, value);
	}

	@Override
	public void setAlignedChar(int byteIndex, char value) {
		store.setChar(byteIndex, value);
	}

	@Override
	public void setAlignedInt(int byteIndex, int value) {
		store.setInt(byteIndex, value);
	}

	@Override
	public void setAlignedLong(int byteIndex, long value) {
		store.setLong(byteIndex, value);
	}

	@Override
	public void setAlignedFloat(int byteIndex, float value) {
		store.setFloat(byteIndex, value);
	}

	@Override
	public void setAlignedDouble(int byteIndex, double value) {
		store.setDouble(byteIndex, value);
	}

	@Override
	public final void clear() {
		store.clear();
	}

	@Override
	public String toBinaryString() {
		return toBinaryString(0);
	}

	@Override
	public String toBinaryString(int splitSize) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < byteSize(); i++) {
			sb.append(BitUtils.toBinaryString(store.getByte(i) & 0xFF, Byte.SIZE, splitSize));
		}
		return sb.toString();
	}

	@Override
	public String toHexString() {
		StringBuilder sb = new StringBuilder();
		for (int i = (int) (endOffset() - 1); i >= 0; i--) {
			sb.insert(0, String.format("%02X", store.getByte(i) & 0xFF));
		}
		return "0x" + sb.toString();
	}

	@Override
	public String toString() {
		return toHexString();
	}

	@Override
	public boolean isReadOnly() {
		return store instanceof ReadOnlyByteStore;
	}
}
