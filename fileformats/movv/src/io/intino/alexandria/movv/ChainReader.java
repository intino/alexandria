package io.intino.alexandria.movv;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Instant;

import static java.lang.System.arraycopy;

interface ChainReader {
    void seek(int cursor) throws IOException;

    Instant readInstant() throws IOException;
    byte[] readData() throws IOException;

    void seekNextOf(int cursor) throws IOException;
    int readNext() throws IOException;

    ChainReader Null = new ChainReader() {
            @Override
            public void seek(int cursor) { }

            @Override
            public Instant readInstant() { return null; }

            @Override
            public byte[] readData() { return new byte[0]; }

            @Override
            public void seekNextOf(int cursor) { }

            @Override
            public int readNext() { return 0; }

        };

    static ChainReader load(byte[] content, int dataSize) {
        return new ChainReader() {
            int position = 0;
            @Override
            public void seek(int cursor) {
                position = positionOf(cursor);
            }

            @Override
            public void seekNextOf(int cursor) {
                position = positionOf(cursor) + Long.BYTES + dataSize;
            }

            @Override
            public Instant readInstant() {
                return Instant.ofEpochMilli(readLong());
            }

            @Override
            public byte[] readData() {
                byte[] bytes = new byte[dataSize];
                arraycopy(content,position,bytes,0,dataSize);
                position+=dataSize;
                return bytes;
            }

            @Override
            public int readNext() {
                return readInt();
            }

            private int positionOf(int cursor) {
                return cursor * recordSize();
            }

            private int readInt() {
                int value = 0;
                for (int i = 0; i < 4; i++)
                    value = (value << 8) + ((int) content[position++] & 0xFF);
                return value;
            }

            private long readLong() {
                long value = 0;
                for (int i = 0; i < 8; i++)
                    value = (value << 8) + ((long) content[position++] & 0xFFL);
                return value;
            }

            private int recordSize() {
                return Long.BYTES + dataSize + Integer.BYTES;
            }

        };
    }

    static ChainReader load(RandomAccessFile raf, int dataSize) {
        return new ChainReader() {
            @Override
            public void seek(int cursor) throws IOException {
                raf.seek(positionOf(cursor));
            }

            @Override
            public void seekNextOf(int cursor) throws IOException {
                raf.seek(positionOf(cursor) + Long.BYTES + dataSize);
            }

            @Override
            public Instant readInstant() throws IOException {
                return Instant.ofEpochMilli(raf.readLong());
            }

            @Override
            public byte[] readData() throws IOException {
                byte[] bytes = new byte[dataSize];
                raf.read(bytes);
                return bytes;
            }

            @Override
            public int readNext() throws IOException {
                return raf.readInt();
            }

            private long positionOf(int cursor) {
                return cursor * recordSize();
            }

            private int recordSize() {
                return Long.BYTES + dataSize + Integer.BYTES;
            }

        };
    }

}
