package io.intino.alexandria.led.buffers.store;


import io.intino.alexandria.led.util.memory.MemoryAddress;
import io.intino.alexandria.led.util.OffHeapObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static io.intino.alexandria.led.util.memory.MemoryUtils.allocBuffer;


public interface ByteStore extends OffHeapObject {

	static ByteStore empty() {
		return new EmptyByteStore();
	}

	static ByteStore wrap(byte[] array, int baseOffset, int size) {
		ByteBuffer buffer = allocBuffer(array.length);
		buffer.put(array).clear();
		return new ByteBufferStore(buffer, MemoryAddress.of(buffer), baseOffset, size);
	}

	ByteOrder order();

	Object storeImpl();

	long baseOffset();

	default long bitCount() {
		return byteSize() * Byte.SIZE;
	}

	default long shortCount() {
		return byteSize() / Short.BYTES;
	}

	default long charCount() {
		return byteSize() / Character.BYTES;
	}

	default long intCount() {
		return byteSize() / Integer.BYTES;
	}

	default long longCount() {
		return byteSize() / Long.BYTES;
	}

	default long floatCount() {
		return byteSize() / Float.BYTES;
	}

	default long doubleCount() {
		return byteSize() / Double.BYTES;
	}

	byte getByte(int byteIndex);

	void setByte(int byteIndex, byte value);

	short getShort(int byteIndex);

	void setShort(int byteIndex, short value);

	char getChar(int byteIndex);

	void setChar(int byteIndex, char value);

	int getInt(int byteIndex);

	void setInt(int byteIndex, int value);

	long getLong(int byteIndex);

	void setLong(int byteIndex, long value);

	float getFloat(int byteIndex);

	void setFloat(int byteIndex, float value);

	double getDouble(int byteIndex);

	void setDouble(int byteIndex, double value);

	void clear();

	ByteStore slice(long baseOffset, long size);
}
