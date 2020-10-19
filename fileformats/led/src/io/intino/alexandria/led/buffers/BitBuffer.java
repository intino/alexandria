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

	long getInteger(int bitIndex, int bitCount);

	float getFloat(int bitIndex);

	double getDouble(int bitIndex);

	void setInteger(int bitIndex, int bitCount, long value);

	void setInt8(int byteIndex, int bitOffset, int bitCount, long value);

	void setInt16(int byteIndex, int bitOffset, int bitCount, long value);

	void setInt32(int byteIndex, int bitOffset, int bitCount, long value);

	void setInt64(int byteIndex, int bitOffset, int bitCount, long value);

	void setFloat(int bitIndex, float value);

	void setDouble(int bitIndex, double value);

	byte getAlignedByte(int byteIndex);

	short getAlignedShort(int byteIndex);

	char getAlignedChar(int byteIndex);

	int getAlignedInt(int byteIndex);

	long getAlignedLong(int byteIndex);

	float getAlignedFloat(int byteIndex);

	double getAlignedDouble(int byteIndex);

	void setAlignedByte(int byteIndex, byte value);

	void setAlignedShort(int byteIndex, short value);

	void setAlignedChar(int byteIndex, char value);

	void setAlignedInt(int byteIndex, int value);

	void setAlignedLong(int byteIndex, long value);

	void setAlignedFloat(int byteIndex, float value);

	void setAlignedDouble(int byteIndex, double value);

	void clear();

	String toBinaryString();

	String toBinaryString(int splitSize);

	String toHexString();

	@Override
	String toString();

	boolean isReadOnly();
}
