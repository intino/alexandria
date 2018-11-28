package io.intino.alexandria.assa;

import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingLong;

public class AssaBuilder<T extends Serializable> {
	private final String name;
	private Index index = new Index();
	private Map<T, Integer> map = new HashMap<>();

	public AssaBuilder(String name) {
		this.name = name;
	}

	public void put(long key, T value) {
		index.put(key, map(value));
	}

	public void put(long[] keys, T value) {
		put(keys, map(value));
	}

	public void save(File file) throws IOException {
		save(new FileOutputStream(file));
	}

	public void save(OutputStream output) throws IOException {
		index.sort();
		try (DataOutputStream target = outputStreamOf(output)) {
			write(target);
		}
	}

	private DataOutputStream outputStreamOf(OutputStream output) {
		return new DataOutputStream(new BufferedOutputStream(output));
	}

	private void write(DataOutputStream output) throws IOException {
		output.writeUTF(name);
		output.writeInt(index.size());
		writeKeyValues(output);
		output.writeInt(map.size());
		writeObjects(objectOutputStream(output));
	}

	private void writeKeyValues(DataOutputStream output) {
		index.tuples.forEach(t -> writeKeyValue(output, t));
	}

	private void writeKeyValue(DataOutputStream output, long[] key) {
		try {
			output.writeLong(key[0]);
			output.writeInt((int) key[1]);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void writeObjects(ObjectOutputStream output) {
		objects().forEach(o -> writeObject(output, o));
	}

	private void writeObject(ObjectOutputStream output, T object) {
		try {
			output.writeObject(object);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private Stream<T> objects() {
		return map.entrySet().stream()
				.sorted(comparing(Entry::getValue))
				.map(Entry::getKey);
	}

	private void put(long[] keys, int value) {
		stream(keys).forEach(k -> index.put(k, value));
	}

	private int map(T object) {
		if (map.containsKey(object)) return map.get(object);
		map.put(object, map.size());
		return map.size() - 1;
	}

	private class Index {
		private final Comparator<long[]> comparator = comparingLong(o -> o[0]);
		private List<long[]> tuples = new ArrayList<>();

		void put(long key, long value) {
			tuples.add(new long[]{key, value});
		}

		private void sort() {
			tuples.sort(comparator);
		}

		public int size() {
			return tuples.size();
		}
	}

	private ObjectOutputStream objectOutputStream(DataOutputStream output) throws IOException {
		return new ObjectOutputStream(new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				output.write(b);
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				output.write(b, off, len);
			}

		});
	}
}
