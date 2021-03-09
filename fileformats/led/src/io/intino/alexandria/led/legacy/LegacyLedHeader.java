package io.intino.alexandria.led.legacy;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class LegacyLedHeader {

    private static final int ELEMENT_COUNT_INDEX = 0;
    private static final int ELEMENT_SIZE_INDEX = ELEMENT_COUNT_INDEX + Long.BYTES;

    public static final int SIZE = ELEMENT_SIZE_INDEX + Long.BYTES;
    public static final long UNKNOWN_SIZE = -1;

    public static LegacyLedHeader from(InputStream inputStream) {
        try {
            byte[] buffer = new byte[SIZE];
            inputStream.read(buffer);
            return new LegacyLedHeader(ByteBuffer.wrap(buffer));
        } catch (IOException e) {
            Logger.error(e);
        }
        return null;
    }


    private final ByteBuffer data;

    public LegacyLedHeader() {
        this.data = ByteBuffer.allocate(SIZE);
        elementCount(UNKNOWN_SIZE);
    }

    public LegacyLedHeader(ByteBuffer data) {
        this.data = requireNonNull(data);
        if(data.capacity() < SIZE) {
            throw new IllegalArgumentException("Header must be " + SIZE + " size in bytes");
        }
    }

    public static LegacyLedHeader from(FileChannel fileChannel) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(SIZE);
            fileChannel.read(buffer);
            return new LegacyLedHeader(buffer.clear());
        } catch(Exception e) {
            Logger.error(e);
        }
        return null;
    }

    public static LegacyLedHeader from(File file) {
        try(FileChannel channel = FileChannel.open(file.toPath())) {
            return from(channel);
        } catch (IOException e) {
            Logger.error(e);
        }
        return null;
    }

    public long elementCount() {
        return data.getLong(ELEMENT_COUNT_INDEX);
    }

    public LegacyLedHeader elementCount(long elementCount) {
        data.putLong(ELEMENT_COUNT_INDEX, elementCount);
        return this;
    }

    public int elementSize() {
        return data.getInt(ELEMENT_SIZE_INDEX);
    }

    public LegacyLedHeader elementSize(int elementSize) {
        data.putInt(ELEMENT_SIZE_INDEX, elementSize);
        return this;
    }

    public ByteBuffer data() {
        return data;
    }

    public byte[] toByteArray() {
        return data.array();
    }

    @Override
    public String toString() {
        return "LedHeader{" +
                "elementCount=" + elementCount() +
                ", elementSize=" + elementSize() +
                '}';
    }
}
