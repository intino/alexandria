package io.intino.alexandria.assa;

import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AssaReader<T extends Serializable> implements AssaStream<T> {
	private final DataInputStream inputStream;
	private final String name;
	private final int size;
	private final List<T> objects;
	private int index;

	public AssaReader(File file) throws IOException, ClassNotFoundException {
		this.inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		this.name = inputStream.readUTF();
		this.size = inputStream.readInt();
		if (size == 0) close();
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
		stream.skipBytes(Character.BYTES + name.length() + Integer.BYTES + size * (Long.BYTES + Integer.BYTES));
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
		try {
			return assaItem(inputStream.readLong(), inputStream.readInt());
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	private Item<T> assaItem(long key, int value) {
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
}
