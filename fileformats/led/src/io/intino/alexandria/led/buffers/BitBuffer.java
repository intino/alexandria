package io.intino.alexandria.led.buffers;


import io.intino.alexandria.led.util.OffHeapObject;

public interface BitBuffer extends OffHeapObject {
	long address();

	void invalidate();

	@Override
	long byteSize();

	long bitCount();

	long baseOffset();

	long endOffset();

	byte getByteNBits(int bitIndex, int bitCount);

	short getShortNBits(int bitIndex, int bitCount);

	int getIntegerNBits(int bitIndex, int bitCount);

	long getLongNBits(int bitIndex, int bitCount);

	float getReal32Bits(int bitIndex);

	double getReal64Bits(int bitIndex);

	void setByteNBits(int bitIndex, int bitCount, byte value);

	void setShortNBits(int bitIndex, int bitCount, short value);

	void setIntegerNBits(int bitIndex, int bitCount, int value);

	void setLongNBits(int bitIndex, int bitCount, long value);

	void setReal32Bits(int bitIndex, float value);

	void setReal64Bits(int bitIndex, double value);

	byte getAlignedByte(int bitIndex);

	short getAlignedShort(int bitIndex);

	char getAlignedChar(int bitIndex);

	int getAlignedInteger(int bitIndex);

	long getAlignedLong(int bitIndex);

	float getAlignedReal32Bits(int bitIndex);

	double getAlignedReal64Bits(int bitIndex);

	void setAlignedByte(int bitIndex, byte value);

	void setAlignedShort(int bitIndex, short value);

	void setAlignedChar(int bitIndex, char value);

	void setAlignedInteger(int bitIndex, int value);

	void setAlignedLong(int bitIndex, long value);

	void setAlignedReal32Bits(int bitIndex, float value);

	void setAlignedReal64Bits(int bitIndex, double value);

	void clear();

	String toBinaryString();

	String toBinaryString(int splitSize);

	String toHexString();

	@Override
	String toString();

	boolean isReadOnly();
}
