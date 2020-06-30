package io.intino.alexandria.mapp;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparingLong;

@SuppressWarnings("WeakerAccess")
public class MappBuilder {
	private final Index index = new Index();
	private final Map<String, Integer> map = new HashMap<>();

	public MappBuilder() {
	}

	public MappBuilder(MappStream stream) {
		put(stream);
	}

	public MappBuilder(List<String> values) {
		values.forEach(this::map);
	}

	public MappBuilder put(long key, String... value) {
		index.put(key, map(String.join("\n", value)));
		return this;
	}

	public MappBuilder put(long[] keys, String... value) {
		put(keys, map(String.join("\n", value)));
		return this;
	}

	public MappBuilder put(long key, List<String> value) {
		index.put(key, map(String.join("\n", value)));
		return this;
	}

	public MappBuilder put(long[] keys, List<String> value) {
		put(keys, map(String.join("\n", value)));
		return this;
	}

	public MappBuilder put(MappStream stream) {
		while (stream.hasNext()) put(stream.next());
		return this;
	}

	public MappBuilder put(MappStream.Item item) {
		index.put(item.key(), map(String.join("\n", item.value())));
		return this;
	}

	public MappBuilder remove(long key) {
		index.remove(key);
		return this;
	}

	public void save(File file) throws IOException {
		save(new FileOutputStream(file));
	}

	private void put(long[] keys, int value) {
		stream(keys).forEach(k -> index.put(k, value));
	}

	private int map(String object) {
		if (map.containsKey(object)) return map.get(object);
		map.put(object, map.size());
		return map.size() - 1;
	}

	public void save(OutputStream output) throws IOException {
		index.sort();
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(output));
		write(dos);
		dos.close();
	}

	private void write(DataOutputStream dos) throws IOException {
		dos.writeInt(map.size());
		for (String value : values()) dos.writeUTF(value);
		dos.writeInt(index.size());
		dos.write(new IndexToByteArray(index).getByteArray());
	}

	private List<String> values() {
		return map.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).map(Map.Entry::getKey).collect(Collectors.toList());
	}

	private static class Index {
		private final Comparator<Mapp.Entry> comparator = comparingLong(o -> o.key);
		private final List<Mapp.Entry> ids = new ArrayList<>();

		void put(long key, int value) {
			ids.add(new Mapp.Entry(key, value));
		}

		private void sort() {
			ids.sort(comparator);
		}

		public int size() {
			return ids.size();
		}

		public void remove(long key) {
			ids.removeAll(ids.stream().filter(i -> i.key == key).collect(Collectors.toList()));
		}
	}

	public static class IndexToByteArray {
		private final Mapp.Entry[] data = new Mapp.Entry[256];
		private final Index index;
		private long base = -1;
		private int count = 0;

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

		private void writeEntry(DataOutputStream stream, Mapp.Entry entry) {
			this.base(stream, entry.key >> 8);
			if (isRepeated((byte) entry.key)) return;
			this.data[this.count++] = entry;
		}

		private boolean isRepeated(byte b) {
			return count > 0 && ((byte) this.data[this.count - 1].key) == b;
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
			int level = this.base >= 0 ? level(base, this.base) : level(base);
			stream.writeByte(level);
			for (int i = level - 1; i >= 0; i--) {
				byte b = (byte) (base >> (i << 3));
				stream.writeByte(b);
			}
		}

		private int level(long base) {
			return base != 0 ? level(base >> 8) + 1 : 0;
		}

		private int level(long a, long b) {
			return a != b ? level(a >> 8, b >> 8) + 1 : 0;
		}

		private void writeData(DataOutputStream stream) throws IOException {
			if (base < 0) return;
			stream.writeByte(count);
			for (int i = 0; i < count; i++) {
				stream.writeByte((byte) data[i].key);
				writeValue(stream, data[i].value);
			}
			count = 0;
		}

		private void writeValue(DataOutputStream stream, long value) throws IOException {
			while (value >= 0x80) {
				stream.write((byte) (0x80 | (value & 0x7F)));
				value = value >> 7;
			}
			stream.write((byte) value);
		}
	}
}
