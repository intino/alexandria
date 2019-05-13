package io.intino.alexandria.movv;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Instant;
import java.util.Arrays;

interface Access {
    Access Null = Null();

    void seek(int cursor) throws IOException;

    Instant readInstant() throws IOException;
    String readData() throws IOException;

    void seekNextOf(int cursor) throws IOException;
    int readNext() throws IOException;
    void writeNext(int cursor) throws IOException;

    static Access Null() {
        return new Access() {

            @Override
            public void seek(int cursor) {

            }

            @Override
            public Instant readInstant() {
                return null;
            }

            @Override
            public String readData() {
                return null;
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

    static Access of(RandomAccessFile raf, int recordLength) {
        return new Access() {
            @Override
            public void seek(int cursor) throws IOException {
                raf.seek(positionOf(cursor));
            }

            @Override
            public void seekNextOf(int cursor) throws IOException {
                raf.seek(positionOf(cursor) + Long.BYTES + recordLength);
            }

            @Override
            public Instant readInstant() throws IOException {
                return Instant.ofEpochMilli(raf.readLong());
            }

            @Override
            public String readData() throws IOException {
                return new String(readBytes());
            }

            @Override
            public int readNext() throws IOException {
                return raf.readInt();
            }

            private long positionOf(int cursor) {
                return cursor * recordSize();
            }

            private byte[] readBytes() throws IOException {
                byte[] bytes = new byte[recordLength];
                raf.read(bytes);
                return Arrays.copyOf(bytes,lengthOf(bytes));
            }

            private int lengthOf(byte[] bytes) {
                int length = 0;
                while ((length < bytes.length) && (bytes[length] != 0)) {
                    length++;
                }
                return length;
            }

            @Override
            public void writeNext(int cursor) throws IOException {
                raf.writeInt(cursor);
            }

            private int recordSize() {
                return Long.BYTES + recordLength + Integer.BYTES;
            }

        };
    }

}
