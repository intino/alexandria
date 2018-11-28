package io.intino.alexandria.zet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class ZOutputStream extends OutputStream {
    private DataOutputStream output;
    private long base = 0;
    private byte[] data = new byte[256];
    private int count = 0;
    private int size = 0;

    public ZOutputStream(OutputStream outputStream) {
        this.output = new DataOutputStream(outputStream);
    }

    public void write(int b) throws IOException {
    }

    public void writeLong(long id) {
        base(id>>8);
        size++;
        data[count++] = (byte) id;
    }

    private void base(long base) {
        try {
            if (this.base == base) return;
            writeData();
            writeLevel(base);
            this.base = base;
        } catch (IOException e) {
        }
    }

    private void writeLevel(long base) throws IOException {
        int level = level(base, this.base);
        output.writeByte(level);
        for (int i = level-1; i >= 0 ; i--) {
            byte b = (byte) (base >> (i << 3));
            output.writeByte(b);
        }
    }

    private int level(long a, long b) {
        return a != b ? level(a >> 8, b >> 8) + 1 : (byte) 0;
    }

    private void writeData() throws IOException {
        output.writeByte(count);
        for (int i = 0; i < count; i++) output.writeByte(data[i]);
        count = 0;
    }

    @Override
    public void close() throws IOException {
        writeData();
        output.writeLong(0xFFFFFFFFFFFFFFFFL);
        output.writeLong(size);
        output.close();
    }
}
