package io.intino.alexandria.assa;

import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingLong;

@SuppressWarnings("WeakerAccess")
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
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(output));
		write(dos);
		dos.close();
	}

	private void write(DataOutputStream dos) throws IOException {
		dos.writeUTF(name);
		byte[] keyBytes = new IndexToByteArray(index).getByteArray();
		dos.writeInt(index.size());
		dos.writeInt(keyBytes.length);
		dos.write(keyBytes);
		dos.writeInt(map.size());
		writeObjects(objectOutputStream(dos));
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
				.sorted(comparing(Map.Entry::getValue))
				.map(Map.Entry::getKey);
	}

	private void put(long[] keys, int value) {
		stream(keys).forEach(k -> index.put(k, value));
	}

	private int map(T object) {
		if (map.containsKey(object)) return map.get(object);
		map.put(object, map.size());
		return map.size() - 1;
	}

	private ObjectOutputStream objectOutputStream(DataOutputStream output) throws IOException {
		return new ObjectOutputStream(new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				output.write(b);
			}

			@SuppressWarnings("NullableProblems")
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				output.write(b, off, len);
			}

		});
	}

	public class IndexToByteArray {
		private long base = -1;
		private AssaEntry[] data = new AssaEntry[256];
		private int count = 0;
		private Index index;

		public IndexToByteArray(Index index) {
			this.index = index;
		}

		private byte[] getByteArray() throws IOException {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(stream);
			index.ids.forEach(entry -> writeEntry(dos, entry));
			writeData(dos);
			dos.close();
			return stream.toByteArray();
		}

		private void writeEntry(DataOutputStream stream, AssaEntry entry) {
			this.base(stream, entry.id >> 8);
			if (isRepeated((byte) entry.id)) return;
			this.data[this.count++] = entry;
		}

		private boolean isRepeated(byte b) {
			return count > 0 && ((byte) this.data[this.count - 1].id) == b;
		}

		private void base(DataOutputStream stream, long base) {
			try {
				if (this.base == base) return;
				writeData(stream);
				writeBase(stream, base);
				this.base = base;
			} catch (IOException ignored) {
			}
		}

		private void writeBase(DataOutputStream stream, long base) throws IOException {
			int level = this.base >= 0 ? level(base, this.base) : 3;
			stream.writeByte(level);
			for (int i = level - 1; i >= 0; i--) {
				byte b = (byte) (base >> (i << 3));
				stream.writeByte(b);
			}
		}

		private int level(long a, long b) {
			return a != b ? level(a >> 8, b >> 8) + 1 : (byte) 0;
		}

		private void writeData(DataOutputStream stream) throws IOException {
			if (base < 0) return;
			stream.writeByte(count);
			for (int i = 0; i < count; i++) {
				stream.writeByte((byte) data[i].id);
				writeValue(stream, data[i].value);
			}
			count = 0;
		}

		private void writeValue(OutputStream stream, long value) throws IOException {
			while (value >= 0x80) {
				stream.write((byte) (0x80 | (value & 0x7F)));
				value = value >> 7;
			}
			stream.write((byte) value);
		}
	}

	private class Index {
		private final Comparator<AssaEntry> comparator = comparingLong(o -> o.id);
		private final List<AssaEntry> ids = new ArrayList<>();

		void put(long key, int value) {
			ids.add(new AssaEntry(key, (short) value));
		}

		private void sort() {
			ids.sort(comparator);
		}

		public int size() {
			return ids.size();
		}

	}
}
