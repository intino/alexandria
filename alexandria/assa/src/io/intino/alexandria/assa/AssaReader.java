package io.intino.alexandria.assa;

import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class AssaReader implements AssaStream {
	final int size;
	final List<String> values;
	final EntryReader entryReader;
	private final DataInputStream inputStream;
	private final String name;
	private int index;

	public AssaReader(File file) throws IOException {
		this(name(file), new FileInputStream(file));
	}

	public AssaReader(String name, InputStream inputStream) throws IOException {
		this.inputStream = new DataInputStream(new BufferedInputStream(inputStream));
		this.name = name;
		this.values = readValues();
		this.index = 0;
		this.size = this.inputStream.readInt();
		this.entryReader = new EntryReader();
		if (size == 0) close();
	}

	private static String name(File file) {
		return file.getParentFile() != null ? file.getParentFile().getName() : file.getName();
	}

	public String name() {
		return name;
	}

	public int size() {
		return size;
	}

	@Override
	public boolean hasNext() {
		return index < size;
	}

	@Override
	public Item next() {
		AssaEntry entry = entryReader.readEntry();
		if (++index >= size) close();
		return assaItem(entry);
	}

	@Override
	public void close() {
		try {
			inputStream.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private List<String> readValues() throws IOException {
		ArrayList<String> values = new ArrayList<>();
		int count = inputStream.readInt();
		for (int i = 0; i < count; i++) values.add(inputStream.readUTF());
		return values;
	}

	private Item assaItem(AssaEntry entry) {
		return new Item() {
			@Override
			public long key() {
				return entry.key;
			}

			@Override
			public List<String> value() {
				String value = values.get(entry.value);
				return value.contains("\n") ? asList(value.split("\n")) : singletonList(value);
			}

			@Override
			public String toString() {
				return entry + "";
			}
		};
	}

	class EntryReader {
		private AssaEntry[] data = new AssaEntry[256];
		private long base = 0;
		private int index = 0;
		private int count = 0;

		EntryReader() {
			this.init();
		}

		private void init() {
			try {
				readBlock();
			} catch (IOException ignored) {
			}
		}

		AssaEntry readEntry() {
			try {
				if (index == count) readBlock();
				return data[index++];
			} catch (IOException e) {
				Logger.error(e);
				return null;
			}
		}

		private void readBlock() throws IOException {
			readBase();
			readData();
			index = 0;
		}

		private void readBase() throws IOException {
			int level = inputStream.read();
			if (level < 0) throw new EOFException();
			this.base = this.base >> (level << 3);
			for (int i = 1; i <= level; i++)
				this.base = (this.base << 8) | (inputStream.readByte() & 0xFF);
		}

		private void readData() throws IOException {
			count = inputStream.readByte() & 0xFF;
			if (count == 0) count = 256;
			for (int i = 0; i < count; i++) {
				byte readByte = inputStream.readByte();
				data[i] = new AssaEntry((this.base << 8) | (readByte & 0xFF), readValue());
			}
		}

		private int readValue() throws IOException {
			long result = 0;
			byte read;
			int shift = 0;
			do {
				read = (byte) inputStream.read();
				result += (read & 0x7FL) << shift;
				shift += 7;
			} while (read < 0);
			return (int) result;
		}


	}
}
