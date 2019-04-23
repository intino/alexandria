package io.intino.alexandria.movv;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.arraycopy;
import static java.util.Arrays.binarySearch;
import static java.util.Comparator.comparing;

interface Index extends Serializable {

    int dataSize();

    int indexOf(long id);
    int headOf(long id);

    void put(long id, int value);

    void store(File file) throws IOException;

    static Index create(int dataSize) {
        return BulkIndex.init(dataSize);
    }

    static Index load(File file) throws IOException {
        return RandomIndex.load(file);
    }

    class RandomIndex implements Index {
        int dataSize;
        long[] ids;
        int[] heads;

        RandomIndex(int dataSize, long[] ids, int[] heads) {
            this.dataSize = dataSize;
            this.ids = ids;
            this.heads = heads;
        }

        static Index load(File file) throws IOException {
            if (!file.exists()) throw new FileNotFoundException(file.getAbsolutePath());
            return load(inputStreamOf(file));
        }

        static ObjectInputStream inputStreamOf(File file) throws IOException {
            return new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
        }

        static Index load(ObjectInputStream ois) throws IOException {
            try {
                return (RandomIndex) ois.readObject();
            } catch (ClassNotFoundException e) {
                return null;
            }
            finally {
                ois.close();
            }
        }

        private static ObjectOutputStream outputStreamOf(File file) throws IOException {
            return new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        }

        @Override
        public int indexOf(long id) {
            return binarySearch(ids, id);
        }

        public int headOf(long id) {
            int idx = indexOf(id);
            return idx < 0 ? -1 : heads[idx];
        }

        @Override
        public int dataSize() {
            return dataSize;
        }

        public void put(long id, int head) {
            int idx = insertionPositionOf(id, 0, size()-1);
            this.ids = insertId(id, idx);
            this.heads = insertHead(head, idx);
        }

        private int insertionPositionOf(long id, int l, int r) {
            if (r < l) return l;
            int m = l + (r - l) / 2;
            return ids[m] > id ?
                    insertionPositionOf(id, l, m - 1) :
                    insertionPositionOf(id, m + 1, r);
        }

        private long[] insertId(long id, int idx) {
            long[] ids = new long[size()+1];
            arraycopy(this.ids,0, ids, 0, idx);
            arraycopy(this.ids, idx, ids, idx+1, this.ids.length-idx);
            ids[idx] = id;
            return ids;
        }

        private int[] insertHead(int head, int idx) {
            int[] heads = new int[this.heads.length+1];
            arraycopy(this.heads,0, heads, 0, idx);
            arraycopy(this.heads, idx, heads, idx+1, this.heads.length-idx);
            heads[idx] = head;
            return heads;
        }

        private int size() {
            return ids.length;
        }

        @Override
        public void store(File file) throws IOException {
            ObjectOutputStream oos = outputStreamOf(file);
            oos.writeObject(this);
            oos.close();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RandomIndex randomIndex = (RandomIndex) o;
            return dataSize == randomIndex.dataSize &&
                    Arrays.equals(ids, randomIndex.ids) &&
                    Arrays.equals(heads, randomIndex.heads);
        }

    }

    class BulkIndex implements Index {
        private int dataSize;
        private List<Tuple> tuples = new ArrayList<>();

        static Index init(int dataSize) {
            return new BulkIndex(dataSize);
        }

        BulkIndex(int dataSize) {
            this.dataSize = dataSize;
        }

        @Override
        public int headOf(long id) {
            return -1;
        }

        @Override
        public int dataSize() {
            return dataSize;
        }

        @Override
        public int indexOf(long id) {
            return -1;
        }

        @Override
        public void put(long id, int head) {
            tuples.add(new Tuple(id, head));
        }

        @Override
        public void store(File file) throws IOException {
            dataIndex().store(file);
        }

        private Index dataIndex() {
            tuples.sort(comparing(t->t.id));
            return new RandomIndex(dataSize, ids(), heads());
        }

        @Override
        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        public boolean equals(Object o) {
            return this == o || dataIndex().equals(o);
        }

        private long[] ids() {
            return tuples.stream().mapToLong(t->t.id).toArray();
        }

        private int[] heads() {
            return tuples.stream().mapToInt(t->t.head).toArray();
        }

        private static class Tuple {
            final long id;
            final int head;

            Tuple(long id, int head) {
                this.id = id;
                this.head = head;
            }
        }
    }
}
