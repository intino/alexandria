package io.intino.alexandria.assa;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class AssaTestWriteAndRead {

	@org.junit.Test
	public void name() throws IOException, ClassNotFoundException {
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
	}

	@Test
	public void test() throws IOException, ClassNotFoundException {
		AssaBuilder<Serializable> test = new AssaBuilder<>("test");
		for (int i = 0; i < 2000000; i++) {
			test.put(i, "test" + i / 10000);
		}
		File file = new File("bigTest.zet");
		test.save(file);

		AssaReader<String> reader = new AssaReader<>(file);
		for (int i = 0; i < 2000000; i++) {
			AssaStream.Item<String> next = reader.next();
			assertEquals(i, next.key());
			assertEquals("test" + i / 10000, next.object());
		}
		assertFalse(reader.hasNext());
	}
}
