package io.intino.alexandria.assa;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class AssaTestWriteAndRead {

	@org.junit.Test
	public void simple_test() throws IOException {
		File file = new File("test.assa");
		AssaBuilder test = new AssaBuilder();
		test.put(1L, "test1");
		test.put(2L, "test2");
		test.put(3L, "test1");
		test.save(file);

		AssaReader reader = new AssaReader(file);
		AssaStream.Item item = reader.next();
		assertEquals(1L, item.key());
		assertEquals(1, item.value().size());
		assertEquals("test1", item.value().get(0));

		item = reader.next();
		assertEquals(2L, item.key());
		assertEquals(1, item.value().size());
		assertEquals("test2", item.value().get(0));

		item = reader.next();
		assertEquals(3L, item.key());
		assertEquals(1, item.value().size());
		assertEquals("test1", item.value().get(0));

		assertFalse(reader.hasNext());
		file.delete();
	}

	@Test
	public void load_test() throws IOException {
		AssaBuilder test = new AssaBuilder();
		for (int i = 0; i < 2000000; i++) {
			test.put(i, "test" + i / 10000);
		}
		File file = new File("bigTest.assa");
		test.save(file);

		AssaReader reader = new AssaReader(file);
		for (int i = 0; i < 2000000; i++) {
			AssaStream.Item next = reader.next();
			assertEquals(i, next.key());
			assertEquals("test" + i / 10000, next.value().get(0));
		}
		assertFalse(reader.hasNext());
//		file.delete();
	}

	@Test
	public void many_values() throws IOException {
		AssaBuilder test = new AssaBuilder();
		for (int i = 0; i < 50000; i++) {
			test.put(i, "test" + i);
		}
		File file = new File("many_values.assa");
		test.save(file);

		AssaReader reader = new AssaReader(file);
		for (int i = 0; i < 50000; i++) {
			AssaStream.Item next = reader.next();
			assertEquals(i, next.key());
			assertEquals("test" + i, next.value().get(0));
		}
		assertFalse(reader.hasNext());
		file.delete();
	}

	@Test
	public void should_work_well_with_multivalued_ids() throws IOException {
		File file = new File("test.assa");
		AssaBuilder test = new AssaBuilder();
		test.put(1L, "test1", "test2");
		test.put(2L, "test2", "test1");
		test.save(file);

		AssaReader reader = new AssaReader(file);
		AssaStream.Item item = reader.next();
		assertEquals(1L, item.key());
		assertEquals(2, item.value().size());
		assertEquals("test1", item.value().get(0));
		assertEquals("test2", item.value().get(1));

		item = reader.next();
		assertEquals(2L, item.key());
		assertEquals(2, item.value().size());
		assertEquals("test2", item.value().get(0));
		assertEquals("test1", item.value().get(1));

		assertFalse(reader.hasNext());
		file.delete();
	}
}
