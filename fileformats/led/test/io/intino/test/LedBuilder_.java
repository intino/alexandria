package io.intino.test;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.LedBuilder;
import io.intino.alexandria.led.LedWriter;
import io.intino.test.transactions.TestTransaction;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Random;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Ignore
public class LedBuilder_ {
	private static final File tempFile = new File("temp/snappy_test.led");
	private static final int NUM_ELEMENTS = 1_000_000;

	@Test
	public void should_build_led() {
		Led<TestTransaction> led = buildLed();
		for(TestTransaction transaction : led) {
			assertNotNull(transaction);
		}
	}

	@Test
	public void should_build_and_write_led() {
		Led<TestTransaction> led = buildLed();
		new LedWriter(tempFile).write(led);
	}

	@Test
	public void should_be_sorted() {
		Led<TestTransaction> led = buildLed();
		long lastId = Long.MIN_VALUE;
		for(int i = 0;i < led.size();i++) {
			final long id = led.transaction(i).id();
			assertTrue(id >= lastId);
			lastId = id;
		}
	}

	private Led<TestTransaction> buildLed() {
		Led.Builder<TestTransaction> builder = Led.builder(TestTransaction.class, TestTransaction::new);
		Random random = new Random();
		for (int i = 0; i < NUM_ELEMENTS; i++) {
			final int id = random.nextInt();
			final int index = i;
			builder.create(t -> t.id(id).b(index - 500).f(index * 100.0 / 20.0));
		}
		return builder.build();
	}

	@After
	public void tearDown() {
		tempFile.delete();
		tempFile.getParentFile().delete();
	}
}
