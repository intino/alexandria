package io.intino.alexandria.led.buffers;

import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.buffers.store.ReadOnlyByteStore;
import io.intino.alexandria.led.util.BitUtils;

import static io.intino.alexandria.led.util.BitUtils.*;
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

	protected abstract BitInfo computeBitInfo(int bitIndex, int bitCount);

	// ================================= BYTE =================================

	@Override
	public byte getByteNBits(int bitIndex, int bitCount) {
		return (byte) extendSign(getNBits(computeBitInfo(bitIndex, bitCount)), bitCount);
	}

	@Override
	public void setByteNBits(int bitIndex, int bitCount, byte value) {
		setNBits(value, computeBitInfo(bitIndex, bitCount));
	}

	@Override
	public short getUByteNBits(int bitIndex, int bitCount) {
		return (short) Byte.toUnsignedInt((byte) getNBits(computeBitInfo(bitIndex, bitCount)));
	}

	@Override
	public void setUByteNBits(int bitIndex, int bitCount, short value) {
		setNBits(value & 0xFF, computeBitInfo(bitIndex, bitCount));
	}

	@Override
	public byte getAlignedByte(int bitIndex) {
		return (byte) extendSign((store.getByte(byteIndex(bitIndex)) & 0xFF), Byte.SIZE);
	}

	@Override
	public void setAlignedByte(int bitIndex, byte value) {
		store.setByte(byteIndex(bitIndex), value);
	}

	@Override
	public short getAlignedUByte(int bitIndex) {
		return (short) (store.getByte(byteIndex(bitIndex)) & 0xFF);
	}

	@Override
	public void setAlignedUByte(int bitIndex, short value) {
		store.setByte(byteIndex(bitIndex), (byte) (value & 0xFF));
	}

	// ================================= SHORT =================================

	@Override
	public short getShortNBits(int bitIndex, int bitCount) {
		final long x = extendSign(getNBits(computeBitInfo(bitIndex, bitCount)), bitCount);
		return (short) x;
	}

	@Override
	public void setShortNBits(int bitIndex, int bitCount, short value) {
		setNBits(value, computeBitInfo(bitIndex, bitCount));
	}

	@Override
	public int getUShortNBits(int bitIndex, int bitCount) {
		return Short.toUnsignedInt((short) getNBits(computeBitInfo(bitIndex, bitCount)));
	}

	@Override
	public void setUShortNBits(int bitIndex, int bitCount, int value) {
		setNBits(value & 0xFFFF, computeBitInfo(bitIndex, bitCount));
	}

	@Override
	public short getAlignedShort(int bitIndex) {
		return (short) extendSign(store.getShort(byteIndex(bitIndex)), Short.SIZE);
	}

	@Override
	public void setAlignedShort(int bitIndex, short value) {
		store.setShort(byteIndex(bitIndex), value);
	}

	@Override
	public int getAlignedUShort(int bitIndex) {
		return Short.toUnsignedInt(store.getShort(byteIndex(bitIndex)));
	}

	@Override
	public void setAlignedUShort(int bitIndex, int value) {
		store.setShort(byteIndex(bitIndex), (short) (value & 0xFFFF));
	}

	// ================================= INT =================================

	@Override
	public int getIntegerNBits(int bitIndex, int bitCount) {
		return (int) extendSign(getNBits(computeBitInfo(bitIndex, bitCount)), bitCount);
	}

	@Override
	public void setIntegerNBits(int bitIndex, int bitCount, int value) {
		setNBits(value, computeBitInfo(bitIndex, bitCount));
	}

	@Override
	public long getUIntegerNBits(int bitIndex, int bitCount) {
		return getNBits(computeBitInfo(bitIndex, bitCount)) & 0xFFFFFFFFL;
	}

	@Override
	public void setUIntegerNBits(int bitIndex, int bitCount, long value) {
		setNBits(value & 0xFFFFFFFFL, computeBitInfo(bitIndex, bitCount));
	}

	@Override
	public int getAlignedInteger(int bitIndex) {
		return (int) extendSign(store.getInt(byteIndex(bitIndex)), Integer.SIZE);
	}

	@Override
	public void setAlignedInteger(int bitIndex, int value) {
		store.setInt(byteIndex(bitIndex), value);
	}

	@Override
	public long getAlignedUInteger(int bitIndex) {
		return store.getInt(byteIndex(bitIndex)) & 0xFFFFFFFFL;
	}

	@Override
	public void setAlignedUInteger(int bitIndex, long value) {
		store.setInt(byteIndex(bitIndex), (int) (value & 0xFFFFFFFFL));
	}

	// ================================= LONG =================================

	@Override
	public long getLongNBits(int bitIndex, int bitCount) {
		return extendSign(getNBits(computeBitInfo(bitIndex, bitCount)), bitCount);
	}

	@Override
	public void setLongNBits(int bitIndex, int bitCount, long value) {
		setNBits(value, computeBitInfo(bitIndex, bitCount));
	}

	@Override
	public long getULongNBits(int bitIndex, int bitCount) {
		if(bitCount == Long.SIZE) throw new UnsupportedOperationException("Unsigned long cannot have " + bitCount + " bits");
		return getNBits(computeBitInfo(bitIndex, bitCount));
	}

	@Override
	public void setULongNBits(int bitIndex, int bitCount, long value) {
		if(bitCount == Long.SIZE) throw new UnsupportedOperationException("Unsigned long cannot have " + bitCount + " bits");
		setNBits(value, computeBitInfo(bitIndex, bitCount));
	}

	@Override
	public long getAlignedLong(int bitIndex) {
		return extendSign(store.getLong(byteIndex(bitIndex)), Long.SIZE);
	}

	@Override
	public void setAlignedLong(int bitIndex, long value) {
		store.setLong(byteIndex(bitIndex), value);
	}

	@Override
	public long getAlignedULong(int bitIndex) {
		return store.getLong(byteIndex(bitIndex)) & 0x7FFFFFFFFFFFFFFFL;
	}

	@Override
	public void setAlignedULong(int bitIndex, long value) {
		store.setLong(byteIndex(bitIndex), value  & 0x7FFFFFFFFFFFFFFFL);
	}

	// ================================= FLOAT =================================

	@Override
	public float getReal32Bits(int bitIndex) {
		BitInfo bitInfo = computeBitInfo(bitIndex, Float.SIZE);
		int bits;
		if(bitInfo.numBytes == Float.BYTES) {
			bits = (int) read(store.getInt(bitInfo.byteIndex), bitInfo.bitOffset, Float.SIZE);
		} else {
			bits = (int) read(store.getLong(bitInfo.byteIndex), bitInfo.bitOffset, Float.SIZE);
		}
		return Float.intBitsToFloat(bits);
	}

	@Override
	public void setReal32Bits(int bitIndex, float value) {
		BitInfo bitInfo = computeBitInfo(bitIndex, Float.SIZE);
		final int bits = Float.floatToIntBits(value);
		if(bitInfo.numBytes == Float.BYTES) {
			final int newValue = (int) write(getAlignedInteger(bitIndex), bits, bitInfo.bitOffset, Float.SIZE);
			store.setInt(bitInfo.byteIndex, newValue);
		} else {
			final long newValue = write(getAlignedLong(bitIndex), bits, bitInfo.bitOffset, Float.SIZE);
			store.setLong(bitInfo.byteIndex, newValue);
		}
	}

	@Override
	public float getAlignedReal32Bits(int bitIndex) {
		return store.getFloat(byteIndex(bitIndex));
	}

	@Override
	public void setAlignedReal32Bits(int bitIndex, float value) {
		store.setFloat(byteIndex(bitIndex), value);
	}

	// ================================= DOUBLE =================================

	@Override
	public double getReal64Bits(int bitIndex) {
		BitInfo bitInfo = computeBitInfo(bitIndex, Double.SIZE);
		final long bits = read(store.getLong(bitInfo.byteIndex), bitInfo.bitOffset, Double.SIZE);
		return Double.longBitsToDouble(bits);
	}

	@Override
	public void setReal64Bits(int bitIndex, double value) {
		BitInfo bitInfo = computeBitInfo(bitIndex, Double.SIZE);
		final long bits = Double.doubleToLongBits(value);
		final long newValue = write(getAlignedLong(bitIndex), bits, bitInfo.bitOffset, Double.SIZE);
		store.setLong(bitInfo.byteIndex, newValue);
	}

	@Override
	public double getAlignedReal64Bits(int bitIndex) {
		return store.getDouble(byteIndex(bitIndex));
	}

	@Override
	public void setAlignedReal64Bits(int bitIndex, double value) {
		store.setDouble(byteIndex(bitIndex), value);
	}

	// ================================= ================================= =================================

	private long getNBits(BitInfo bitInfo) {
		long value;
		final int bitIndex = bitInfo.bitIndex;
		switch(bitInfo.numBytes) {
			case Byte.BYTES:
				value = getAlignedByte(bitIndex);
				value &= 0xFF;
				break;
			case Short.BYTES:
				value = getAlignedShort(bitIndex);
				value &= 0xFFFF;
				break;
			case Integer.BYTES:
				value = getAlignedInteger(bitIndex);
				value &= 0xFFFFFFFF;
				break;
			case Long.BYTES:
				value = getAlignedLong(bitIndex);
				break;
			default:
				throw misAlignmentError(bitInfo);
		}
		return read(value, bitInfo.bitOffset, bitInfo.bitCount);
	}

	private void setNBits(long value, BitInfo bitInfo) {
		final int bitIndex = bitInfo.bitIndex;
		switch(bitInfo.numBytes) {
			case Byte.BYTES:
				setInt8(bitIndex, bitInfo.byteIndex, bitInfo.bitOffset, bitInfo.bitCount, value);
				break;
			case Short.BYTES:
				setInt16(bitIndex, bitInfo.byteIndex, bitInfo.bitOffset, bitInfo.bitCount, value);
				break;
			case Integer.BYTES:
				setInt32(bitIndex, bitInfo.byteIndex, bitInfo.bitOffset, bitInfo.bitCount, value);
				break;
			case Long.BYTES:
				setInt64(bitIndex, bitInfo.byteIndex, bitInfo.bitOffset, bitInfo.bitCount, value);
				break;
			default:
				throw misAlignmentError(bitInfo);
		}
	}

	private UnsupportedOperationException misAlignmentError(BitInfo bitInfo) {
		return new UnsupportedOperationException("Value will use " + bitInfo.numBytes + " bytes due to misalignment." +
				" Align the value to a bit position multiple of " + Long.SIZE);
	}

	private void setInt8(int bitIndex, int byteIndex, int bitOffset, int bitCount, long value) {
		final byte oldValue = getAlignedByte(bitIndex);
		final byte newValue = (byte)(write(oldValue, (byte)value, bitOffset, bitCount));
		store.setByte(byteIndex, newValue);
	}

	private void setInt16(int bitIndex,int byteIndex, int bitOffset, int bitCount, long value) {
		final short oldValue = getAlignedShort(bitIndex);
		final short newValue = (short)(write(oldValue, (short)value, bitOffset, bitCount));
		store.setShort(byteIndex, newValue);
	}

	private void setInt32(int bitIndex,int byteIndex, int bitOffset, int bitCount, long value) {
		final int oldValue = getAlignedInteger(bitIndex);
		final int newValue = (int)(write(oldValue, (int)value, bitOffset, bitCount));
		store.setInt(byteIndex, newValue);
	}

	private void setInt64(int bitIndex, int byteIndex, int bitOffset, int bitCount, long value) {
		final long oldValue = getAlignedLong(bitIndex);
		final long newValue = write(oldValue, value, bitOffset, bitCount);
		store.setLong(byteIndex, newValue);
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
		return toBinaryString(8);//toHexString();
	}

	@Override
	public boolean isReadOnly() {
		return store instanceof ReadOnlyByteStore;
	}

	public static final class BitInfo {

		private final short bitIndex;
		private final short byteIndex;
		private final short numBytes;
		private final short bitOffset;
		private final short bitCount;

		public BitInfo(int bitIndex, int byteIndex, int numBytes, int bitOffset, int bitCount) {
			this.bitIndex = (short) bitIndex;
			this.byteIndex = (short) byteIndex;
			this.numBytes = (short) numBytes;
			this.bitOffset = (short) bitOffset;
			this.bitCount = (short) bitCount;
		}

		@Override
		public String toString() {
			return "{" +
					"bitIndex=" + bitIndex +
					", byteIndex=" + byteIndex +
					", numBytes=" + numBytes +
					", bitOffset=" + bitOffset +
					", bitCount=" + bitCount +
					'}';
		}
	}
}
