package io.intino.alexandria.assa.loaders;

import io.intino.alexandria.assa.Assa;
import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.lang.reflect.Array;

public class InputStreamAssa<T extends Serializable> implements Assa<T> {
	private final Class<T> type;
	private final String name;
	private final int size;
	private final long[] keys;
	private final int[] values;
	private final T[] objects;

	public static <X extends Serializable> InputStreamAssa<X> of(InputStream input, Class<X> type) throws IOException {
		try {
			return new InputStreamAssa<>(type, new DataInputStream(input));
		} catch (ClassNotFoundException e) {
			Logger.error(e);
			return new InputStreamAssa<>(type);
		}
	}

	InputStreamAssa(Class<T> type, DataInputStream input) throws IOException, ClassNotFoundException {
		this.type = type;
		this.name = input.readUTF();
		this.size = input.readInt();
		this.keys = new long[size];
		this.values = new int[size];
		this.readKeyValues(input);
		this.objects = readObjects(input);
	}

	@SuppressWarnings("unchecked")
	InputStreamAssa(Class<T> type) {
		this.type = type;
		this.name = type.getName();
		this.size = 0;
		this.keys = new long[0];
		this.values = new int[0];
		this.objects = (T[]) new Object[0];
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public int size() {
		return keys.length;
	}

	@Override
	public T get(long key) {
		return getObject(indexOf(key));
	}

	private int indexOf(long key) {
		int low = 0;
		int high = keys.length - 1;
		while (low <= high) {
			int i = (low + high) >>> 1;
			long cmp = keys[i] - key;
			if (cmp == 0) return i;
			if (cmp < 0) low = i + 1;
			else high = i - 1;
		}
		return -1;
	}

	private T getObject(int idx) {
		return idx >= 0 ? objects[values[idx]] : null;
	}

	private void readKeyValues(DataInputStream input) throws IOException {
		for (int i = 0; i < size; i++) {
			keys[i] = input.readLong();
			values[i] = input.readInt();
		}
	}

	private T[] readObjects(DataInputStream input) throws IOException, ClassNotFoundException {
		int count = input.readInt();
		return load(objectInputStream(input), count);
	}

	private ObjectInputStream objectInputStream(DataInputStream input) throws IOException {
		return new ObjectInputStream(input);
	}

	@SuppressWarnings("unchecked")
	private T[] load(ObjectInputStream input, int count) throws IOException, ClassNotFoundException {
		T[] objects = array(type, count);
		for (int i = 0; i < objects.length; i++)
			objects[i] = (T) input.readObject();
		return objects;
	}

	@SuppressWarnings("unchecked")
	private static <T> T[] array(Class<T> type, int length) {
		return (T[]) Array.newInstance(type, length);
	}


}
