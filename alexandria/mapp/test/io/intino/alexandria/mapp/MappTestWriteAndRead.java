package io.intino.alexandria.mapp;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class MappTestWriteAndRead {

	@Test
	public void simple_test() throws IOException {
		File file = new File("test.mapp");
		MappBuilder test = new MappBuilder();
		test.put(1L, "test1");
		test.put(2L, "test2");
		test.put(3L, "test1");
		test.save(file);

		MappReader reader = new MappReader(file);
		MappStream.Item item = reader.next();
		assertEquals(1L, item.key());
		assertEquals("test1", item.value());

		item = reader.next();
		assertEquals(2L, item.key());
		assertEquals("test2", item.value());

		item = reader.next();
		assertEquals(3L, item.key());
		assertEquals("test1", item.value());

		assertFalse(reader.hasNext());
		file.delete();
	}

	@Test
	public void simple_test2() throws IOException {
		File file = new File("test2.mapp");
		MappBuilder test = new MappBuilder();
		test.put(2L, "a");
		test.put(3L, "b");
		test.put(4L, "c");
		test.save(file);

		MappReader reader = new MappReader(file);
		MappStream.Item item = reader.next();
		assertEquals(2L, item.key());
		assertEquals("a", item.value());

		item = reader.next();
		assertEquals(3L, item.key());
		assertEquals("b", item.value());
		item = reader.next();
		assertEquals(4L, item.key());
		assertEquals("c", item.value());

		assertFalse(reader.hasNext());
		file.delete();
	}

	@Test
	public void read_not_exiting_file() throws IOException {
		MappReader reader = new MappReader(new File("foo"));
		assertFalse(reader.hasNext());
	}

	@Test
	public void load_test() throws IOException {
		MappBuilder test = new MappBuilder();
		for (int i = 0; i < 2000000; i++) {
			test.put(i, "test" + i / 10000);
		}
		File file = new File("bigTest.mapp");
		test.save(file);

		MappReader reader = new MappReader(file);
		for (int i = 0; i < 2000000; i++) {
			MappStream.Item next = reader.next();
			assertEquals(i, next.key());
			assertEquals("test" + i / 10000, next.value());
		}
		assertFalse(reader.hasNext());
		file.delete();
	}

	@Test
	public void many_values() throws IOException {
		MappBuilder test = new MappBuilder();
		for (int i = 0; i < 50000; i++) {
			test.put(i, "test" + i);
		}
		File file = new File("many_values.mapp");
		test.save(file);

		MappReader reader = new MappReader(file);
		for (int i = 0; i < 50000; i++) {
			MappStream.Item next = reader.next();
			assertEquals(i, next.key());
			assertEquals("test" + i, next.value());
		}
		assertFalse(reader.hasNext());
		file.delete();
	}

	@Test
	public void should_work_well_with_multivalued_ids() throws IOException {
		File file = new File("test.mapp");
		MappBuilder test = new MappBuilder();
		test.put(1L, "test1", "test2");
		test.put(2L, "test2", "test1");
		test.save(file);

		MappReader reader = new MappReader(file);
		MappStream.Item item = reader.next();
		assertEquals(1L, item.key());
		assertEquals("test1\ntest2", item.value());

		item = reader.next();
		assertEquals(2L, item.key());
		assertEquals("test2\ntest1", item.value());

		assertFalse(reader.hasNext());
//		file.delete();
	}
}
