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
		this.objects = size != 0 ? readObjects(dataInputStream(new RandomAccessFile(file, "r"))) : null;
		this.index = 0;
	}

	public String name() {
		return name;
	}

	@SuppressWarnings("unchecked")
	private List<T> readObject(ObjectInputStream objectInputStream, int count) throws IOException, ClassNotFoundException {
		List<T> objects = new ArrayList<>(count);
		for (int i = 0; i < count; i++)
			objects.add((T) objectInputStream.readObject());
		return objects;
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


	private DataInputStream dataInputStream(RandomAccessFile file) throws IOException {
		file.seek(Integer.BYTES + name.length() + Long.BYTES + size * (Long.BYTES + Integer.BYTES));
		return new DataInputStream(new InputStream() {
			@Override
			public int read() throws IOException {
				return file.read();
			}

			@Override
			public int read(byte[] b) throws IOException {
				return file.read(b);
			}
		});
	}

	private List<T> readObjects(DataInputStream inputStream) throws IOException, ClassNotFoundException {
		int count = inputStream.readInt();
		return readObject(new ObjectInputStream(inputStream), count);
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
		};
	}

	private T getObject(int value) {
		return objects.get(value);
	}

	private void close() {
		try {
			inputStream.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

}
