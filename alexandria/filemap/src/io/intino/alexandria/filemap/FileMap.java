package io.intino.alexandria.filemap;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;

import static java.util.Comparator.comparingLong;

public class FileMap {
	private Index index;
	private Data data;
	private final Object object = new Object();

	public FileMap() {
		this.index = new Index();
		this.data = new Data();
	}

	public void put(String key, String value) {
		synchronized (object) {
			index.put(key, data.put(value));
		}
	}

	public String get(String key) {
		synchronized (object) {
			return data.get(index.get(key));
		}
	}

	public String getOrDefault(String key, String defaultValue) {
		synchronized (object) {
			return containsKey(key) ? data.get(index.get(key)) : defaultValue;
		}
	}

	public int size() {
		synchronized (object) {
			return index.size();
		}
	}

	public boolean isEmpty() {
		synchronized (object) {
			return index.isEmpty();
		}
	}

	public boolean containsKey(String key) {
		synchronized (object) {
			return index.containsKey(key);
		}
	}

	public void close() {
		data.close();
	}

	private static class Index {
		private static final Comparator<long[]> comparator = comparingLong(o -> o[0]);
		private static ArrayList<long[]> tuples = new ArrayList<>();
		private boolean sorted = false;

		void put(String key, long value) {
			sorted = false;
			put(hashCodeOf(key), value);
		}

		long get(String key) {
			if (!sorted) sort();
			return get(hashCodeOf(key));
		}

		private void put(long... item) {
			tuples.add(item);
		}

		private void sort() {
			tuples.sort(comparator);
			sorted = true;
		}

		private long get(long key) {
			int low = 0;
			int high = tuples.size() - 1;
			while (low <= high) {
				int idx = (low + high) >>> 1;
				long[] item = tuples.get(idx);
				long cmp = item[0] - key;
				if (cmp < 0) low = idx + 1;
				else if (cmp > 0) high = idx - 1;
				else return item[1];
			}
			return -1;
		}

		private long hashCodeOf(String key) {
			long h = 1125899906842597L;
			int len = key.length();

			for (int i = 0; i < len; i++) h = 31 * h + key.charAt(i);
			return h;
		}

		int size() {
			return tuples.size();
		}

		boolean isEmpty() {
			return tuples.isEmpty();
		}

		boolean containsKey(String key) {
			return get(key) != -1;
		}
	}

	private static class Data {
		private RandomAccessFile file;

		Data() {
			try {
				this.file = new RandomAccessFile(temp(), "rw");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		long put(String value) {
			try {
				long pos = pos();
				file.seek(pos);
				file.writeUTF(value);
				return pos;
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
		}

		String get(long pos) {
			if (pos < 0) return null;
			try {
				file.seek(pos);
				return file.readUTF();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		void close() {
			try {
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		private File temp() throws IOException {
			File tempFile = File.createTempFile("fileMap", ".db");
			tempFile.deleteOnExit();
			return tempFile;
		}

		private long pos() throws IOException {
			return file.length();
		}
	}
}
