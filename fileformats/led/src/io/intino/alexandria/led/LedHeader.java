package io.intino.alexandria.led;

import io.intino.alexandria.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static java.util.Objects.requireNonNull;

public class LedHeader {

    public static final int SIZE = Long.BYTES + Integer.BYTES;
    public static final long UNKNOWN_SIZE = -1;

    public static LedHeader from(InputStream inputStream) {
        try {
            byte[] buffer = new byte[SIZE];
            inputStream.read(buffer);
            return new LedHeader(ByteBuffer.wrap(buffer));
        } catch (IOException e) {
            Logger.error(e);
        }
        return null;
    }


    private final ByteBuffer data;

    public LedHeader() {
        this.data = ByteBuffer.allocate(SIZE);
        elementCount(UNKNOWN_SIZE);
    }

    public LedHeader(ByteBuffer data) {
        this.data = requireNonNull(data);
        if(data.capacity() < SIZE) {
            throw new IllegalArgumentException("Header must be " + SIZE + " size in bytes");
        }
    }

    public long elementCount() {
        return data.getLong(0);
    }

    public LedHeader elementCount(long elementCount) {
        data.putLong(0, elementCount);
        return this;
    }

    public int elementSize() {
        return data.getInt(Long.BYTES);
    }

    public LedHeader elementSize(int elementSize) {
        data.putInt(Long.BYTES, elementSize);
        return this;
    }

    public byte[] toByteArray() {
        return data.array();
    }
}
