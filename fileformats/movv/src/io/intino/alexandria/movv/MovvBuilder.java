package io.intino.alexandria.movv;

import java.io.*;
import java.time.Instant;
import java.util.*;

import static io.intino.alexandria.movv.Movv.indexOf;
import static java.lang.Integer.min;
import static java.lang.System.arraycopy;

public class MovvBuilder {
    private final File file;
    private final Index index;
    private final Writer writer;
    private final Map<Long, Stage> stages;

    private interface Writer {
        int write(Instant instant, String data, boolean isTheLast) throws IOException;
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
        this.stages = new HashMap<>();
    }

    private boolean isStatic(Index index) {
        return index instanceof Index.BulkIndex;
    }

    public Stage stageOf(long id)  {
        if (!stages.containsKey(id)) stages.put(id, createStage(id));
        return stages.get(id);

    }

    private Stage createStage(long id) {
        return new Stage() {
            List<Object[]> items = new ArrayList<>();
            @Override
            public Stage add(Instant instant, String data) {
                items.add(new Object[]{instant, data});
                return this;
            }

            @Override
            public MovvBuilder commit() throws IOException {
                items.sort(Comparator.comparing(o -> ((Instant) o[0])));
                return update(new Mov(index, access()).of(id));
            }

            private MovvBuilder update(Mov mov) throws IOException {
                items = clean(items, isUpdatingFile() ? lastDataOf(mov) : null);
                int i = 0;
                for (Object[] item : items) {
                    int cursor = write(item, ++i == items.size());
                    if (i == 1) mov.append(id, cursor);
                }
                return MovvBuilder.this;
            }

            private int write(Object[] item, boolean isTheLast) throws IOException {
                return writer.write((Instant) item[0], (String) item[1], isTheLast);
            }

            private List<Object[]> clean(List<Object[]> items, String data) {
                List<Object[]> result = new ArrayList<>();
                for (Object[] item : items) {
                    if (item[1].equals(data)) continue;
                    result.add(item);
                    data = (String) item[1];
                }
                return result;
            }

        };
    }

    public interface Stage {
        Stage add(Instant instant, String data);
        MovvBuilder commit() throws IOException;
    }


    public MovvBuilder add(long id, Instant instant, String data) throws IOException {
        Mov mov = new Mov(index, access()).of(id);
        if (isUpdatingFile() && data.equals(lastDataOf(mov))) return this;
        mov.append(id, writer.write(instant, data, true));
        return this;
    }

    private String lastDataOf(Mov mov) throws IOException {
        return mov.last();
    }

    private Access access() {
        return writer instanceof RandomWriter ?
                Access.of(((RandomWriter) writer).raf, index.dataSize()) :
                Access.Null;
    }

    private boolean isCreatingFile() {
        return index instanceof Index.BulkIndex;
    }

    private boolean isUpdatingFile() {
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
        public int write(Instant instant, String data, boolean isTheLast) throws IOException {
            os.writeLong(instant.toEpochMilli());
            os.write(toByteArray(data));
            os.writeInt(isTheLast ? -1 : cursor + 1);
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
        public int write(Instant instant, String data, boolean isTheLast) throws IOException {
            raf.seek(raf.length());
            raf.writeLong(instant.toEpochMilli());
            raf.write(toByteArray(data));
            raf.writeInt(isTheLast ? -1 : cursor + 1);
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
