package io.intino.test;

import io.intino.alexandria.tabb.Row;
import io.intino.alexandria.tabb.Tabb;
import io.intino.alexandria.tabb.TabbReader;
import io.intino.alexandria.tabb.Value;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TabbTest {
	private File bigSource = new File("test-res/tabbs/big.tabb");
	private File otherSource = new File("test-res/tabbs/other.tabb");
	private File smallSource = new File("test-res/tabbs/small.tabb");
	private File test = new File(bigSource.getParentFile(), "test.tabb");
	private File smallTest = new File(smallSource.getParentFile(), "smallTest.tabb");
	private File otherTest = new File("test-res/tabbs/otherTest.tabb");

	@Before
	public void setUp() throws Exception {
		Files.copy(bigSource.toPath(), new File(bigSource.getParentFile(), "test.tabb").toPath(), StandardCopyOption.REPLACE_EXISTING);
		Files.copy(smallSource.toPath(), new File(smallSource.getParentFile(), "smallTest.tabb").toPath(), StandardCopyOption.REPLACE_EXISTING);
		Files.copy(otherSource.toPath(), new File(otherSource.getParentFile(), "otherTest.tabb").toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	@Test
	@Ignore
	public void readTest() throws IOException {
		TabbReader tabb = new TabbReader(otherSource);
		while (tabb.hasNext()) {
			Row next = tabb.next();
			System.out.println(next.get(0).asInstant());
			System.out.println(next.get(1).asString());
			System.out.println(next.get(2).asString());
			System.out.println(next.get(3).asString());
			System.out.println(next.get(4).asDouble());
			System.out.println();
		}
	}

	@Test
	public void updateTest() throws IOException {
		Tabb tabb = new Tabb(test);
		tabb.update("201806C85CCO8605231N4", new ArrayList<>(tabb.manifest().columns()).get(6), 1L);
	}

	@Test
	public void removeTest() throws IOException {
		new Tabb(test).remove("201806C85CCO8605231N4");
	}

	@Test
	public void removeAndIterateTest() throws IOException {
		Tabb tabb = new Tabb(test).remove("201806C85CCO8605231N4");
		AtomicInteger count = new AtomicInteger(0);
		tabb.forEachRemaining(r -> count.getAndIncrement());
		Assert.assertEquals(10995, count.get());
	}

	@Test
	public void updateAndIterateTest() throws IOException {
		Tabb tabb = new Tabb(test);
		tabb = tabb.update("201806C85CCO8605231N4", new ArrayList<>(tabb.manifest().columns()).get(6), 1L);
		tabb.forEachRemaining(r -> {
			if (r.get(0).asObject().toString().equals("201806") && r.get(4).asObject().toString().equals("C85") && r.get(5).asObject().toString().equals("CCO8605231N4"))
				Assert.assertEquals(r.get(6).asLong().longValue(), 1L);
		});
	}

	@Test
	public void appendFullTest() throws IOException {
		Tabb tabb = new Tabb(otherTest).append(new Object[]{
				"201912",
				Instant.now(),
				Instant.now(),
				Instant.now(),
				"M001",
				"342352344",
				"342352344",
				2345L,
				234235432L,
				234235432L,
				16,
				"01",
				"43534543",
				"43534543",
				"201911",
				null});
	}

	@Test
	public void appendTest() throws IOException {
		new Tabb(smallTest).append(new Object[]{"201806", Instant.now(), "C85", "CCO8605231N4", 1L});
	}

	@Test
	public void appendAndIterateTest() throws IOException {
		//Remove 2 and append 1
		AtomicInteger count = new AtomicInteger(0);
		Tabb tabb = new Tabb(smallTest).append(new Object[]{"201806", Instant.now(), "C85", "CCO8605231N4", 1L});
		tabb.forEachRemaining(r -> count.getAndIncrement());
		Assert.assertEquals(19, count.get());
	}

	@Test
	public void appendAndConsolidateTest() throws IOException {
		//Remove 2 and append 1
		Tabb tabb = new Tabb(smallTest).append(new Object[]{"201806", Instant.now(), "C85", "CCO8605231N4", 1L}).consolidate();
	}

	@Test
	public void updateAndRemove() throws IOException {
		Tabb tabb = new Tabb(test);
		tabb.update("201806C85CCO8605231N4", new ArrayList<>(tabb.manifest().columns()).get(6), 1L).remove("201806C85CCO8605231N4");
	}

	@Test
	public void consolidateTest() throws IOException {
		Tabb tabb = new Tabb(otherTest);
		tabb.consolidate();
	}

	@Test
	public void updateAndConsolidate() throws IOException {
		Tabb tabb = new Tabb(test);
		tabb.update("201806C85CCO8605231N4", new ArrayList<>(tabb.manifest().columns()).get(6), 1L).consolidate();
	}

	@Test
	public void removeAndConsolidate() throws IOException {
		Tabb tabb = new Tabb(test).remove("201806C85CCO8605231N4").consolidate();
		Assert.assertEquals(10995, tabb.manifest().size());
		AtomicInteger count = new AtomicInteger(0);
		tabb.forEachRemaining(r -> count.getAndIncrement());
		Assert.assertEquals(10995, count.get());
	}

	@Test
	@Ignore
	public void bigSource() throws IOException {
		Tabb tabb = new Tabb(bigSource);
		tabb.forEachRemaining(this::print);
	}

	@Test
	@Ignore
	public void smallSource() throws IOException {
		Tabb tabb = new Tabb(smallSource);
		tabb.forEachRemaining(this::print);
	}

	private void print(Row r) {
		for (Value value : r) {
			String s = (value == null || value.asObject() == null ? "null" : value.asObject().toString()) + "; ";
			System.out.print(s);
		}
		System.out.println();
	}

	@Test
	@Ignore
	public void test() throws IOException {
		Tabb tabb = new Tabb(test);
		tabb.forEachRemaining(this::print);
	}

	@After
	public void tearDown() throws Exception {
		Files.move(test.toPath(), new File(test.getParentFile(), "test.zip").toPath(), StandardCopyOption.REPLACE_EXISTING);
		Files.move(smallTest.toPath(), new File(smallTest.getParentFile(), "smallTest.zip").toPath(), StandardCopyOption.REPLACE_EXISTING);
		Files.move(otherTest.toPath(), new File(otherTest.getParentFile(), "otherTest.zip").toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
}
