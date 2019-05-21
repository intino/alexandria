package io.intino.alexandria.movv;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Instant;

interface ChainReader {
    void seek(int cursor) throws IOException;

    Instant readInstant() throws IOException;
    byte[] readData() throws IOException;

    void seekNextOf(int cursor) throws IOException;
    int readNext() throws IOException;
    void writeNext(int cursor) throws IOException;

    static ChainReader Null() {
        return new ChainReader() {

            @Override
            public void seek(int cursor) {

            }

            @Override
            public Instant readInstant() {
                return null;
            }

            @Override
            public byte[] readData() {
                return new byte[0];
            }

            @Override
            public void seekNextOf(int cursor) {

            }

            @Override
            public int readNext() {
                return 0;
            }

            @Override
            public void writeNext(int cursor) {

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

            @Override
            public void writeNext(int cursor) throws IOException {
                raf.writeInt(cursor);
            }

            private int recordSize() {
                return Long.BYTES + dataSize + Integer.BYTES;
            }

        };
    }

}
