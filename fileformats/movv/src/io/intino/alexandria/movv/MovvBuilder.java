package io.intino.alexandria.movv;

import java.io.*;
import java.time.Instant;

import static io.intino.alexandria.movv.Movv.indexOf;
import static java.lang.Integer.min;
import static java.lang.System.arraycopy;

public class MovvBuilder {
    private final File file;
    private final Index index;
    private final Writer writer;

    private interface Writer {
        int write(Instant instant, String data) throws IOException;
        void close() throws IOException;
    }

    public static MovvBuilder create(File file, int dataSize) throws IOException {
        return new MovvBuilder(file, Index.create(dataSize));
    }

    public static MovvBuilder update(File file) throws IOException {
        return new MovvBuilder(file, Index.load(indexOf(file)));
    }

    private MovvBuilder(File file, Index index) throws IOException {
        this.file = file;
        this.index = index;
        this.writer = isStatic(index) ? new BulkWriter(file) : new RandomWriter(new RandomAccessFile(file, "rw"));
    }

    private boolean isStatic(Index index) {
        return index instanceof Index.BulkIndex;
    }

    public MovvBuilder add(long id, Instant instant, String data) throws IOException {
        Mov mov = new Mov(index, access()).of(id);
        if (isUpdating() && data.equals(mov.last())) return this;
        mov.link(id, writer.write(instant, data));
        return this;
    }

    private Access access() {
        return writer instanceof RandomWriter ?
                Access.of(((RandomWriter) writer).raf, index.dataSize()) :
                Access.Null;
    }

    private boolean isUpdating() {
        return index instanceof Index.RandomIndex;
    }

    private byte[] toByteArray(String data) {
        byte[] bytes = new byte[index.dataSize()];
        arraycopy(data.getBytes(),0,bytes,0,min(index.dataSize(), data.length()));
        return bytes;
    }

    public void close() throws IOException {
        index.store(indexOf(file));
        writer.close();
    }

    private class BulkWriter implements Writer {
        private final DataOutputStream os;
        private int cursor;

        BulkWriter(File file) throws FileNotFoundException {
            this.os = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            this.cursor = 0;
        }

        @Override
        public int write(Instant instant, String data) throws IOException {
            os.writeLong(instant.toEpochMilli());
            os.write(toByteArray(data));
            os.writeInt(-1);
            return cursor++;
        }

        @Override
        public void close() throws IOException {
            os.close();
        }
    }

    private class RandomWriter implements Writer {
        private final RandomAccessFile raf;
        private int cursor;

        RandomWriter(RandomAccessFile raf) throws IOException {
            this.raf = raf;
            this.cursor = (int) (raf.length() / recordSize());
        }

        @Override
        public int write(Instant instant, String data) throws IOException {
            raf.seek(raf.length());
            raf.writeLong(instant.toEpochMilli());
            raf.write(toByteArray(data));
            raf.writeInt(-1);
            return cursor++;
        }

        @Override
        public void close() throws IOException {
            raf.close();
        }

    }

    private int recordSize() {
        return Long.BYTES + index.dataSize() + Integer.BYTES;
    }
}
