package io.intino.alexandria.assa;

import java.io.File;
import java.io.IOException;

public class Assa {
	private final int size;
	private final String[] values;
	private AssaEntry[]  entries;

	public Assa(File file) throws IOException {
		this(new AssaReader(file));
	}

	public Assa(AssaReader reader) throws IOException {
		this.values = reader.values.toArray(new String[0]);
		this.size = reader.size;
		this.entries = readEntries(reader);
		reader.close();
	}

	private AssaEntry[] readEntries(AssaReader reader) {
		AssaEntry[] entries = new AssaEntry[size];
		for (int i = 0; i < size; i++)
			entries[i] = reader.entryReader.readEntry();
		return entries;
	}

	public int size() {
		return size;
	}

	public String get(long key) {
		return values[entries[indexOf(key)].value];
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



}
