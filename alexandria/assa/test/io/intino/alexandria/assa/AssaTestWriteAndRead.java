package io.intino.alexandria.assa;

import org.junit.Test;

import java.io.*;
import java.util.Arrays;

import static java.util.Arrays.copyOf;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class AssaTestWriteAndRead {

	@org.junit.Test
	public void simple_test() throws IOException, ClassNotFoundException {
		File file = new File("test.assa");
		AssaBuilder<String> test = new AssaBuilder<>("test");
		test.put(1L, "test1");
		test.put(2L, "test2");
		test.put(3L, "test1");
		test.save(file);

		AssaReader<String> reader = new AssaReader<>(file);
		AssaStream.Item<String> item = reader.next();
		assertEquals(1L, item.key());
		assertEquals("test1", item.object());

		item = reader.next();
		assertEquals(2L, item.key());
		assertEquals("test2", item.object());

		item = reader.next();
		assertEquals(3L, item.key());
		assertEquals("test1", item.object());

		assertFalse(reader.hasNext());
		file.delete();
	}

	@Test
	public void load_test() throws IOException, ClassNotFoundException {
		AssaBuilder<Serializable> test = new AssaBuilder<>("test");
		for (int i = 0; i < 2000000; i++) {
			test.put(i, "test" + i / 10000);
		}
		File file = new File("bigTest.assa");
		test.save(file);

		AssaReader<String> reader = new AssaReader<>(file);
		for (int i = 0; i < 2000000; i++) {
			AssaStream.Item<String> next = reader.next();
			assertEquals(i, next.key());
			assertEquals("test" + i / 10000, next.object());
		}
		assertFalse(reader.hasNext());
		file.delete();
	}

	@Test
	public void many_values() throws IOException, ClassNotFoundException {
		AssaBuilder<Serializable> test = new AssaBuilder<>("test");
		for (int i = 0; i < 50000; i++) {
			test.put(i, "test" + i);
		}
		File file = new File("many_values.assa");
		test.save(file);

		AssaReader<String> reader = new AssaReader<>(file);
		for (int i = 0; i < 50000; i++) {
			AssaStream.Item<String> next = reader.next();
			assertEquals(i, next.key());
			assertEquals("test" + i, next.object());
		}
		assertFalse(reader.hasNext());
		file.delete();
	}

	@Test
	public void test_assa_writer() throws IOException, ClassNotFoundException {
		File file = new File("output.assa");
		AssaWriter<String> writer = new AssaWriter<>(file);
		writer.save("output", new AssaStream<String>() {
			int i = 1;
			@Override
			public int size() {
				return 3;
			}

			@Override
			public Item<String> next() {
				return new Item<String>() {
					@Override
					public long key() {
						return i;
					}

					@Override
					public String object() {
						return "test" + i++;
					}
				};
			}

			@Override
			public boolean hasNext() {
				return i <= size();
			}

			@Override
			public void close() {
			}
		});

		AssaReader<String> reader = new AssaReader<>(file);
		AssaStream.Item<String> item = reader.next();
		assertEquals(1L, item.key());
		assertEquals("test1", item.object());

		item = reader.next();
		assertEquals(2L, item.key());
		assertEquals("test2", item.object());

		item = reader.next();
		assertEquals(3L, item.key());
		assertEquals("test3", item.object());

		assertFalse(reader.hasNext());
		file.delete();
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		assertEquals((byte) 1,  compress(1).length);
		assertEquals((byte) 1, compress(1)[0]);
		assertEquals((byte) 2  ,compress(128).length);
		assertEquals((byte) 0x80, compress(128)[0]);
		assertEquals((byte) 0x01, compress(128)[1]);
		assertEquals((byte) 5 ,compress(0x205B9EA3).length);
		assertEquals((byte) 0xA3, compress(0x205B9EA3)[0]);
		assertEquals((byte) 0xBD, compress(0x205B9EA3)[1]);
		assertEquals((byte) 0xEE, compress(0x205B9EA3)[2]);
		assertEquals((byte) 0x82, compress(0x205B9EA3)[3]);
		assertEquals((byte) 0x2, compress(0x205B9EA3)[4]);

		assertEquals(0x205B9EA3, decompress(compress(0x205B9EA3)));
		byte[] compress = compress(0x8D8FD8F5DFD5ADAL);
		assertEquals(0x8D8FD8F5DFD5ADAL, decompress(compress));
	}

	private static long decompress(byte[] bytes) throws IOException {
		return decompress(new ByteArrayInputStream(bytes));
	}

	private static byte[] compress(long value) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		compress(value, stream);
		return stream.toByteArray();
	}


	private static void compress(long value, OutputStream stream) throws IOException {
		while (value >= 0x80) {
			stream.write((byte) (0x80 | (value & 0x7F)));
			value = value >> 7;
		}
		stream.write((byte) value);
	}

	private static long decompress(InputStream stream) throws IOException {
		long result = 0;
		byte read;
		int shift = 0;
		do {
			read = (byte) stream.read();
			result += (read & 0x7FL) << shift;
			shift += 7;
		} while (read < 0);
		return result;
	}
}
