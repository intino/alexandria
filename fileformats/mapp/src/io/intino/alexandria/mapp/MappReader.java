package io.intino.alexandria.mapp;

import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MappReader implements MappStream {
	final int size;
	final List<String> labels;
	final EntryReader entryReader;
	private final DataInputStream inputStream;
	private final String name;
	private int index;

	public MappReader(File file) throws IOException {
		this(baseName(file.getName()), file.exists() ? new FileInputStream(file) : emptyMappFile());
	}

	public MappReader(String name, InputStream inputStream) throws IOException {
		this.inputStream = new DataInputStream(new BufferedInputStream(inputStream));
		this.name = name;
		this.labels = readLabels();
		this.index = 0;
		this.size = this.inputStream.readInt();
		this.entryReader = new EntryReader();
		if (size == 0) close();
	}

	private static ByteArrayInputStream emptyMappFile() {
		return new ByteArrayInputStream(new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
	}

	private static String baseName(String file) {
		return file.substring(0, file.lastIndexOf("."));
	}

	public String name() {
		return name;
	}

	public int size() {
		return size;
	}

	public List<String> labels() {
		return labels;
	}

	@Override
	public boolean hasNext() {
		return index < size;
	}

	@Override
	public Item next() {
		Mapp.Entry entry = entryReader.readEntry();
		if (++index >= size) close();
		return mappItem(entry);
	}

	@Override
	public void close() {
		try {
			inputStream.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private List<String> readLabels() throws IOException {
		ArrayList<String> values = new ArrayList<>();
		int count = inputStream.readInt();
		for (int i = 0; i < count; i++) values.add(inputStream.readUTF());
		return values;
	}

	private Item mappItem(Mapp.Entry entry) {
		return new Item() {
			@Override
			public long key() {
				return entry.key;
			}

			@Override
			public String value() {
				int value = entry.value;
				return value == -1 ? null : labels.get(value);
			}

			@Override
			public String toString() {
				return entry + "";
			}
		};
	}

	class EntryReader {

		private Mapp.Entry[] data = new Mapp.Entry[256];
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

		Mapp.Entry readEntry() {
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
				data[i] = new Mapp.Entry((this.base << 8) | (readByte & 0xFF), readValue());
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
