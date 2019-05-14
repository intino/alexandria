package io.intino.alexandria.movv;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.arraycopy;
import static java.util.Arrays.binarySearch;
import static java.util.Collections.emptyIterator;
import static java.util.Comparator.comparing;

interface ChainIndex extends Serializable, Iterable<Long> {
	long serialVersionUID = -256880122051640503L;

	static ChainIndex create(File file, int dataSize) {
		return BulkChainIndex.init(file, dataSize);
	}

	static ChainIndex load(File file) throws IOException {
		return RandomChainIndex.load(file);
	}

	int dataSize();

	int length();

	boolean contains(long id);

	int indexOf(long id);

	int headOf(long id);

	void put(long id, int value);

	void close() throws IOException;

	class RandomChainIndex implements ChainIndex {
		int dataSize;
		long[] ids;
		int[] heads;
		private transient File file;

		RandomChainIndex() {
		}

		RandomChainIndex(File file, int dataSize, long[] ids, int[] heads) {
			this.file = file;
			this.dataSize = dataSize;
			this.ids = ids;
			this.heads = heads;
		}

		static ChainIndex load(File file) throws IOException {
			if (!file.exists()) throw new FileNotFoundException(file.getAbsolutePath());
			return deserialize(file);
		}

		static ChainIndex deserialize(File file) throws IOException {
			Input input = new Input(new FileInputStream(file));
			RandomChainIndex index = getKryo().readObject(input, RandomChainIndex.class);
			input.close();
			index.file = file;
			return index;
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
			int idx = insertionPositionOf(id, 0, length() - 1);
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
			long[] ids = new long[length() + 1];
			arraycopy(this.ids, 0, ids, 0, idx);
			arraycopy(this.ids, idx, ids, idx + 1, this.ids.length - idx);
			ids[idx] = id;
			return ids;
		}

		private int[] insertHead(int head, int idx) {
			int[] heads = new int[this.heads.length + 1];
			arraycopy(this.heads, 0, heads, 0, idx);
			arraycopy(this.heads, idx, heads, idx + 1, this.heads.length - idx);
			heads[idx] = head;
			return heads;
		}

		public int length() {
			return ids.length;
		}

		@Override
		public boolean contains(long id) {
			return indexOf(id) >= 0;
		}

		@Override
		public void close() throws IOException {
			Output output = new Output(new FileOutputStream(file));
			getKryo().writeObject(output, this);
			output.close();
		}

		private static Kryo getKryo() {
			Kryo kryo = new Kryo();
			kryo.register(RandomChainIndex.class);
			kryo.register(int[].class);
			kryo.register(long[].class);
			return kryo;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			RandomChainIndex randomIndex = (RandomChainIndex) o;
			return dataSize == randomIndex.dataSize &&
					Arrays.equals(ids, randomIndex.ids) &&
					Arrays.equals(heads, randomIndex.heads);
		}

		@Override
		public Iterator<Long> iterator() {
			return new Iterator<Long>() {
				int i = 0;

				@Override
				public boolean hasNext() {
					return i < ids.length;
				}

				@Override
				public Long next() {
					return ids[i++];
				}
			};
		}
	}

	class BulkChainIndex implements ChainIndex {
		private final File file;
		private int dataSize;
		private List<Tuple> tuples = new ArrayList<>();

		BulkChainIndex(File file, int dataSize) {
			this.file = file;
			this.dataSize = dataSize;
		}

		static ChainIndex init(File file, int dataSize) {
			return new BulkChainIndex(file, dataSize);
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
		public void close() throws IOException {
			randomChainIndex().close();
		}

		@Override
		public int length() {
			return -1;
		}

		@Override
		public boolean contains(long id) {
			return false;
		}

		private ChainIndex randomChainIndex() {
			tuples.sort(comparing(t -> t.id));
			return new RandomChainIndex(file, dataSize, ids(), heads());
		}

		@Override
		@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
		public boolean equals(Object o) {
			return this == o || randomChainIndex().equals(o);
		}

		private long[] ids() {
			return tuples.stream().mapToLong(t -> t.id).toArray();
		}

		private int[] heads() {
			return tuples.stream().mapToInt(t -> t.head).toArray();
		}

		@Override
		public Iterator<Long> iterator() {
			return emptyIterator();
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
