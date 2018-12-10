package io.intino.alexandria.assa;

import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AssaReader<T extends Serializable> implements AssaStream<T> {
	private final DataInputStream inputStream;
	private final String name;
	private final int size;
	private final int bytesLength;
	private final List<T> objects;
	private final IndexReader indexReader;
	private int index;

	public AssaReader(File file) throws IOException, ClassNotFoundException {
		this.inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		this.name = inputStream.readUTF();
		this.size = inputStream.readInt();
		this.bytesLength = inputStream.readInt();
		if (size == 0) close();
		this.indexReader = new IndexReader(inputStream);
		this.objects = size != 0 ? readObjects(new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) : null;
		this.index = 0;
	}

	public String name() {
		return name;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean hasNext() {
		return this.index < size;
	}

	@Override
	public Item<T> next() {
		return closingIfLastItemIs(index++ < size ? nextItem() : null);
	}

	@Override
	public void close() {
		try {
			inputStream.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private List<T> readObjects(DataInputStream stream) throws IOException, ClassNotFoundException {
		stream.skipBytes(Character.BYTES + name.length() + 2 * Integer.BYTES + bytesLength);
		int count = stream.readInt();
		return readObject(new ObjectInputStream(stream), count);
	}

	@SuppressWarnings("unchecked")
	private List<T> readObject(ObjectInputStream objectInputStream, int count) throws IOException, ClassNotFoundException {
		List<T> objects = new ArrayList<>(count);
		for (int i = 0; i < count; i++)
			objects.add((T) objectInputStream.readObject());
		return objects;
	}

	private Item<T> closingIfLastItemIs(Item<T> next) {
		if (index == size) close();
		return next;
	}

	private Item<T> nextItem() {
		AssaEntry entry = indexReader.readEntry();
		return assaItem(entry.id, entry.value);
	}

	private Item<T> assaItem(long key, short value) {
		return new Item<T>() {
			@Override
			public long key() {
				return key;
			}

			@Override
			public T object() {
				return getObject(value);
			}

			@Override
			public String toString() {
				return key + "";
			}
		};
	}

	private T getObject(int value) {
		return objects.get(value);
	}

	@Override
	public String toString() {
		return name;
	}

	class IndexReader {
		private final DataInputStream input;
		private long base = 0;
		private AssaEntry[] data = new AssaEntry[256];
		private int count = 0;
		private int index = 0;
		private int size = 0;

		IndexReader(DataInputStream inputStream) {
			this.input = inputStream;
			this.init();
		}

		private void init() {
			try {
				readBlock();
			} catch (IOException ignored) {
			}
		}

		public int size() {
			return size;
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
		}

		private void readBase() throws IOException {
			int level = input.read();
			if (level < 0) throw new EOFException();
			this.base = this.base >> (level << 3);
			for (int i = 1; i <= level; i++)
				this.base = (this.base << 8) | (input.readByte() & 0xFF);
		}

		private void readData() throws IOException {
			count = input.readByte() & 0xFF;
			if (count == 0) count = 256;
			size += count;
			index = 0;
			for (int i = 0; i < count; i++) {
				byte readByte = input.readByte();
				data[i] = new AssaEntry((this.base << 8) | (readByte & 0xFF), input.readShort());
			}
		}
	}
}
