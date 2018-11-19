package io.intino.alexandria.assa.loaders;

import io.intino.alexandria.assa.Assa;

import java.io.*;
import java.lang.reflect.Array;

import static java.util.Arrays.binarySearch;

public class FileLazyAssa<T extends Serializable> implements Assa<T> {
	private final Class<T> type;
	private final String name;
	private final int size;
	private final KeyValue keyValue;
	private final T[] objects;

	public static <X extends Serializable> FileLazyAssa<X> of(File file, Class<X> type) throws IOException {
		try {
			return new FileLazyAssa<>(type, file);
		} catch (ClassNotFoundException e) {
			//TODO Logger.log(e);
			return new FileLazyAssa<>(type);
		}
	}

	private FileLazyAssa(Class<T> type, File file) throws IOException, ClassNotFoundException {
		DataInputStream input = dataOf(file);
		this.type = type;
		this.name = input.readUTF();
		this.size = input.readInt();
		this.keyValue = new KeyValue(new RandomAccessFile(file, "r"), this.name, this.size);
		this.objects = readObjects(input);
	}

	private static DataInputStream dataOf(File file) throws FileNotFoundException {
		return new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
	}

	private FileLazyAssa(Class<T> type) {
		this.type = type;
		this.name = type.getName();
		this.size = 0;
		this.keyValue = null;
		this.objects = null;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public T get(long key) {
		return getObject(keyValue.get(key));
	}

	private T getObject(int idx) {
		return idx >= 0 ? objects[idx] : null;
	}

	private T[] readObjects(DataInputStream input) throws IOException, ClassNotFoundException {
		input.skipBytes(size * (Long.BYTES + Integer.BYTES));
		int count = input.readInt();
		return load(objectInputStream(input), count);
	}

	private ObjectInputStream objectInputStream(DataInputStream input) throws IOException {
		return new ObjectInputStream(input);
	}

	@SuppressWarnings("unchecked")
	private T[] load(ObjectInputStream input, int count) throws IOException, ClassNotFoundException {
		T[] objects = (T[]) Array.newInstance(type, count);
		for (int i = 0; i < count; i++)
			objects[i] = (T) input.readObject();
		return objects;
	}

	private class KeyValue {
		private final RandomAccessFile file;
		private final int offset;
		private final int size;
		private final Cache cache;

		public KeyValue(RandomAccessFile file, String name, int size) {
			this.file = file;
			this.offset = 6 + name.getBytes().length;
			this.size = size;
			this.cache = new Cache(1000);
		}

		int get(long key) {
			int[] range = cache.rangeOf(key);
			return valueOf(range[0], range[0] == range[1] ? 0 : binarySearch(cache.keysIn(range), key));
		}

		private int valueOf(int index, int offset) {
			return offset >= 0 ? valueOf(index + offset) : -1;
		}

		private int valueOf(int index) {
			try {
				file.seek(offset + (index << 3) + (index << 2) + Long.BYTES);
				return file.readInt();
			} catch (IOException e) {
				return -1;
			}
		}

		private class Cache {
			private final int step;
			private final long tabs[];
			private int tab = -1;
			private long[] keys;

			public Cache(int step) {
				this.step = step;
				this.tabs = keysEvery(step);
			}

			private long[] keysEvery(int step) {
				int i = 0;
				long[] keys = new long[size / step + (size % step == 0 ? 0 : 1)];
				for (int index = 0; index < size; index += step) keys[i++] = readKey(index);
				return keys;
			}

			public long[] keysIn(int[] range) {
				if (range[0] != tab) keys = readKeys(range);
				return keys;
			}

			private long[] readKeys(int[] range) {
				tab = range[0];
				return read(offset + positionOf(range[0]), range[1] - range[0]);
			}

			private long readKey(int index) {
				return read(offset + positionOf(index), 1)[0];
			}

			private long[] read(int start, int size) {
				try {
					DataInputStream input = inputStream(start, size);
					long[] result = new long[size];
					for (int i = 0; i < size; i++) {
						result[i] = input.readLong();
						input.readInt();
					}
					return result;
				} catch (IOException e) {
					return new long[0];
				}
			}

			private DataInputStream inputStream(int start, int size) throws IOException {
				byte[] buffer = new byte[positionOf(size)];
				file.seek(start);
				file.read(buffer);
				return new DataInputStream(new ByteArrayInputStream(buffer));
			}

			private int positionOf(int i) {
				return (i << 3) + (i << 2);
			}

			public int[] rangeOf(long key) {
				int i = search(key);
				return tabs[i] == key ?
						new int[]{i * step, i * step} :
						new int[]{i * step + 1, i == tabs.length - 1 ? size : (i + 1) * step};
			}

			private int search(long key) {
				int low = 0;
				int high = tabs.length - 1;
				while (true) {
					if (high == low) return low;
					int mid = (low + high) >> 1;
					if (key >= tabs[mid] && key < tabs[mid + 1]) return mid;
					if (key < tabs[mid]) high = mid - 1;
					if (key > tabs[mid]) low = mid + 1;
					if (high < low) return mid;
				}
			}
		}


	}


}
