package io.intino.alexandria.assa;

import io.intino.alexandria.logger.Logger;

import java.io.*;

public class AssaReader<T extends Serializable> implements AssaStream<T> {
	private ObjectInputStream inputStream = null;
	private int size;
	private int current = 0;
	private Item<T> next = null;

	public AssaReader(File file) {
		try {
			seek(new DataInputStream(new FileInputStream(file)));
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Item<T> next() {
		if (this.next == null) return null;
		Item<T> current = this.next;
		this.next = nextItem();
		return current;
	}

	@Override
	public boolean hasNext() {
		if (current > size) {
			close();
			return false;
		}
		return true;
	}

	private void seek(DataInputStream stream) throws IOException {
		readHeader(stream);
		this.inputStream = new ObjectInputStream(stream);
		this.next = nextItem();
	}

	private void readHeader(DataInputStream stream) throws IOException {
		stream.readUTF();
		int size = stream.readInt();
		stream.skipBytes(8 * size + 4 * size);
		this.size = stream.readInt();
	}

	private AssaItem nextItem() {
		try {
			if (current == size) return null;
			return new AssaItem(current++, readObject());
		} catch (IOException | ClassNotFoundException e) {
			Logger.error(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private T readObject() throws IOException, ClassNotFoundException {
		return (T) inputStream.readObject();
	}

	private void close() {
		try {
			inputStream.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	public class AssaItem implements Item<T> {
		private final long id;
		private final T object;

		AssaItem(long id, T item) {
			this.id = id;
			this.object = item;
		}

		@Override
		public long key() {
			return id;
		}

		@Override
		public T object() {
			return object;
		}
	}
}
