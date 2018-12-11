package io.intino.alexandria.assa;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

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
		AssaReader<Serializable> reader = new AssaReader<>(new File("D:/201811.assa"));
		while(reader.hasNext()){
			AssaStream.Item<Serializable> next = reader.next();
			System.out.println(next.key() + ";" + next.object());
		}
	}
}
