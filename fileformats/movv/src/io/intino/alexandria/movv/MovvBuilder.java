package io.intino.alexandria.movv;

import io.intino.alexandria.movv.Mov.Item;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

import static io.intino.alexandria.movv.Movv.indexOf;
import static java.lang.Integer.min;
import static java.lang.System.arraycopy;
import static java.util.Comparator.comparing;

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

    public Mov movOf(long id) {
        return new Mov(index, access()).of(id);
    }

    public Stage stageOf(long id) {
        return createIfNotExist(id).get(id);
    }

    public Stream<Stage> stages() {
        return new ArrayList<>(stages.values()).stream();
    }

    public MovvBuilder add(long id, Instant instant, String data)  {
        try {
            Mov mov = movOf(id);
            if (isUpdatingFile() && mov.reject(instant, data)) return this;
            mov.append(id, writer.write(instant, data, true));
        } catch (IOException ignored) {
        }
        return this;
    }

    public void close() throws IOException {
        index.store(indexOf(file));
        writer.close();
    }

    public interface Stage {
        long id();
        Stage add(Instant instant, String data);
        MovvBuilder commit();
    }

    private Map<Long, Stage> createIfNotExist(long id) {
        if (!stages.containsKey(id)) stages.put(id, createStage(id));
        return stages;
    }

    private boolean isStatic(Index index) {
        return index instanceof Index.BulkIndex;
    }

    private Stage createStage(long id) {
        return new Stage() {
            List<Item> items = new ArrayList<>();

            @Override
            public long id() {
                return id;
            }

            @Override
            public Stage add(Instant instant, String data) {
                items.add(new Item(instant, data));
                return this;
            }

            @Override
            public MovvBuilder commit() {
                stages.remove(id);
                items.sort(comparing(o -> o.instant));
                update(movOf(id));
                return MovvBuilder.this;
            }

            private void update(Mov mov) {
                try {
                    items = clean(items, isUpdatingFile() ? lastDataOf(mov) : null);
                    int i = 0;
                    for (Item item : items) {
                        int cursor = write(item, ++i == items.size());
                        if (i == 1) mov.append(id, cursor);
                    }
                } catch (IOException ignored) {
                }
            }

            private int write(Item item, boolean isTheLast) throws IOException {
                return writer.write(item.instant, item.data, isTheLast);
            }

            private List<Item> clean(List<Item> items, String data) {
                List<Item> result = new ArrayList<>();
                for (Item item : items) {
                    if (item.data.equals(data)) continue;
                    result.add(item);
                    data = item.data;
                }
                return result;
            }

        };
    }

    private String lastDataOf(Mov mov) {
        return mov.last().data;
    }

    private Access access() {
        return writer instanceof RandomWriter ?
                Access.of(((RandomWriter) writer).raf, index.dataSize()) :
                Access.Null;
    }

    private boolean isUpdatingFile() {
        return index instanceof Index.RandomIndex;
    }

    private byte[] toByteArray(String data) {
        byte[] bytes = new byte[index.dataSize()];
        arraycopy(data.getBytes(),0,bytes,0,min(index.dataSize(), data.length()));
        return bytes;
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
