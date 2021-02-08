package io.intino.alexandria.led.buffers.store;

import java.nio.ByteOrder;

public class EmptyByteStore implements ByteStore {

    @Override
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }

    @Override
    public Object storeImpl() {
        return this;
    }

    @Override
    public long address() {
        return NULL;
    }

    @Override
    public long byteSize() {
        return 0;
    }

    @Override
    public long baseOffset() {
        return 0;
    }

    @Override
    public byte getByte(int byteIndex) {
        return 0;
    }

    @Override
    public void setByte(int byteIndex, byte value) {

    }

    @Override
    public short getShort(int byteIndex) {
        return 0;
    }

    @Override
    public void setShort(int byteIndex, short value) {

    }

    @Override
    public char getChar(int byteIndex) {
        return 0;
    }

    @Override
    public void setChar(int byteIndex, char value) {

    }

    @Override
    public int getInt(int byteIndex) {
        return 0;
    }

    @Override
    public void setInt(int byteIndex, int value) {

    }

    @Override
    public long getLong(int byteIndex) {
        return 0;
    }

    @Override
    public void setLong(int byteIndex, long value) {

    }

    @Override
    public float getFloat(int byteIndex) {
        return 0;
    }

    @Override
    public void setFloat(int byteIndex, float value) {

    }

    @Override
    public double getDouble(int byteIndex) {
        return 0;
    }

    @Override
    public void setDouble(int byteIndex, double value) {

    }

    @Override
    public void clear() {

    }

    @Override
    public ByteStore slice(long baseOffset, long size) {
        return new EmptyByteStore();
    }
}
