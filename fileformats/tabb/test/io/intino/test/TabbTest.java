package io.intino.test;

import io.intino.alexandria.tabb.Row;
import io.intino.alexandria.tabb.Tabb;
import io.intino.alexandria.tabb.TabbReader;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class TabbTest {

	private File bigSource = new File("test-res/tabbs/big.tabb");
	private File readSource = new File("test-res/tabbs/201903_2.tabb");
	private File smallSource = new File("test-res/tabbs/small.tabb");
	private File test = new File(bigSource.getParentFile(), "test.tabb");
	private File smallTest = new File(smallSource.getParentFile(), "smallTest.tabb");

	@Before
	public void setUp() throws Exception {
		Files.copy(bigSource.toPath(), new File(bigSource.getParentFile(), "test.tabb").toPath(), StandardCopyOption.REPLACE_EXISTING);
		Files.copy(smallSource.toPath(), new File(smallSource.getParentFile(), "smallTest.tabb").toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	@Test
	@Ignore
	public void readTest() throws IOException {
		TabbReader tabb = new TabbReader(readSource);
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
		tabb.update("201806C85CCO8605231N4", tabb.manifest().columns()[6], 1L);
	}

	@Test
	public void removeTest() throws IOException {
		new Tabb(test).remove("201806C85CCO8605231N4");
	}

	@Test
	public void appendTest() throws IOException {
		new Tabb(smallTest).append(new Object[]{"201806", Instant.now(), "C85", "CCO8605231N4", 1L});
	}

	@Test
	public void appendAndConsolidateTest() throws IOException {
		//Remove 2 and append 1
		Tabb tabb = new Tabb(smallTest).append(new Object[]{"201806", Instant.now(), "C85", "CCO8605231N4", 1L}).consolidate();
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
	public void bigSource() throws IOException {
		Tabb tabb = new Tabb(bigSource);
		tabb.iterator().forEachRemaining(this::print);
	}

	@Test
	@Ignore
	public void smallSource() throws IOException {
		Tabb tabb = new Tabb(smallSource);
		tabb.iterator().forEachRemaining(this::print);
	}

	private void print(Row r) {
		System.out.println(r.get(0).asString() + " : " + r.get(1).asInstant() + " : " + r.get(2).asString() + " : " + r.get(3).asString() + " : " + r.get(4).asLong());
	}

	@Test
	@Ignore
	public void test() throws IOException {
		Tabb tabb = new Tabb(test);
		tabb.iterator().forEachRemaining(this::print);
	}

	@After
	public void tearDown() throws Exception {
		Files.move(test.toPath(), new File(test.getParentFile(), "test.zip").toPath(), StandardCopyOption.REPLACE_EXISTING);
		Files.move(smallTest.toPath(), new File(smallTest.getParentFile(), "smallTest.zip").toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
}
