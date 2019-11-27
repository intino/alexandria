package io.intino.test;

import io.intino.alexandria.tabb.Row;
import io.intino.alexandria.tabb.Tabb;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicInteger;

public class TabbTest {

	private File source = new File("test-res/tabbs/example.tabb");
	private File test = new File(source.getParentFile(), "test.tabb");

	@Before
	public void setUp() throws Exception {
		Files.copy(source.toPath(), new File(source.getParentFile(), "test.tabb").toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	@Test
	public void updateTest() throws IOException {
		Tabb tabb = new Tabb(test);
		tabb.update("201806C85CCO8605231N4", tabb.manifest().columns()[6], 1L);
	}

	@Test
	public void removeTest() throws IOException {
		new Tabb(test).remove("201806C85CCO8605231N4");
	}

	@Test
	public void updateAndRemove() throws IOException {
		Tabb tabb = new Tabb(test);
		tabb.update("201806C85CCO8605231N4", tabb.manifest().columns()[6], 1L).remove("201806C85CCO8605231N4");
	}

	@Test
	public void updateAndConsolidate() throws IOException {
		Tabb tabb = new Tabb(test);
		tabb.update("201806C85CCO8605231N4", tabb.manifest().columns()[6], 1L).consolidate();
	}

	@Test
	public void removeAndConsolidate() throws IOException {
		Tabb tabb = new Tabb(test).remove("201806C85CCO8605231N4").consolidate();
		Assert.assertEquals(10995, tabb.manifest().size());
		AtomicInteger count = new AtomicInteger(0);
		tabb.iterator().forEachRemaining(r -> count.getAndIncrement());
		Assert.assertEquals(10995, count.get());
	}

	@Test
	@Ignore
	public void original() throws IOException {
		Tabb tabb = new Tabb(source);
		tabb.iterator().forEachRemaining(this::print);
	}

	private void print(Row r) {
		System.out.println(r.get(0).asString() + " : " + r.get(1).asInstant() + " : " + r.get(4).asString() + " : " + r.get(5).asString() + " : " + r.get(6).asLong() + " : " + r.get(9).asInteger());
	}

	@Test
	@Ignore
	public void test() throws IOException {
		Tabb tabb = new Tabb(test);
		tabb.iterator().forEachRemaining(this::print);
	}

	@After
	public void tearDown() throws Exception {
		Files.copy(test.toPath(), new File(test.getParentFile(), "test.zip").toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
}
