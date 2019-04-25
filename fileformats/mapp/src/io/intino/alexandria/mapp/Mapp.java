package io.intino.alexandria.mapp;

import java.io.File;
import java.io.IOException;

public class Mapp {
	private final int size;
	private final String[] values;
	private Entry[] entries;

	public Mapp(File file) throws IOException {
		this(new MappReader(file));
	}

	public Mapp(MappReader reader) {
		this.values = reader.labels.toArray(new String[0]);
		this.size = reader.size;
		this.entries = readEntries(reader);
		reader.close();
	}

	private Entry[] readEntries(MappReader reader) {
		Entry[] entries = new Entry[size];
		for (int i = 0; i < size; i++)
			entries[i] = reader.entryReader.readEntry();
		return entries;
	}

	public int size() {
		return size;
	}

	public String get(long key) {
		int i = indexOf(key);
		if (i < 0) return null;
		return values[entries[i].value];
	}

	private int indexOf(long key) {
		int low = 0;
		int high = entries.length - 1;
		while (low <= high) {
			int i = (low + high) >>> 1;
			long cmp = entries[i].key - key;
			if (cmp == 0) return i;
			if (cmp < 0) low = i + 1;
			else high = i - 1;
		}
		return -1;
	}


	static class Entry {
		long key;
		int value;

		Entry(long key, int value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public int hashCode() {
			return Long.hashCode(key);
		}
	}
}
