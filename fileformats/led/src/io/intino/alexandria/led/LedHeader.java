package io.intino.alexandria.led;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class LedHeader {

    private static final int ELEMENT_COUNT_INDEX = 0;
    private static final int ELEMENT_SIZE_INDEX = ELEMENT_COUNT_INDEX + Long.BYTES;
    private static final int UUID_HIGH_INDEX = ELEMENT_SIZE_INDEX + Integer.BYTES;
    private static final int UUID_LOW_INDEX = UUID_HIGH_INDEX + Long.BYTES;

    public static final int SIZE = UUID_LOW_INDEX + Long.BYTES;
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

    public static LedHeader from(FileChannel fileChannel) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(SIZE);
            fileChannel.read(buffer);
            return new LedHeader(buffer.clear());
        } catch(Exception e) {
            Logger.error(e);
        }
        return null;
    }

    public static LedHeader from(File file) {
        try(FileChannel channel = FileChannel.open(file.toPath())) {
            return from(channel);
        } catch (IOException e) {
            Logger.error(e);
        }
        return null;
    }

    public static void write(LedHeader header, RandomAccessFile file) throws IOException {
        file.write(header.toByteArray());
    }

    public long elementCount() {
        return data.getLong(ELEMENT_COUNT_INDEX);
    }

    public LedHeader elementCount(long elementCount) {
        data.putLong(ELEMENT_COUNT_INDEX, elementCount);
        return this;
    }

    public int elementSize() {
        return data.getInt(ELEMENT_SIZE_INDEX);
    }

    public LedHeader elementSize(int elementSize) {
        data.putInt(ELEMENT_SIZE_INDEX, elementSize);
        return this;
    }

    public UUID uuid() {
        return new UUID(data.getLong(UUID_HIGH_INDEX), data.getLong(UUID_LOW_INDEX));
    }

    public LedHeader uuid(UUID uuid) {
        data.putLong(UUID_HIGH_INDEX, uuid.getMostSignificantBits());
        data.putLong(UUID_LOW_INDEX, uuid.getLeastSignificantBits());
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
                ", uuid=" + uuid() +
                '}';
    }
}
