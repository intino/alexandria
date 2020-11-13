package io.intino.test;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.LedBuilder;
import io.intino.alexandria.led.LedWriter;
import io.intino.test.transactions.TestTransaction;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

@Ignore
public class LedBuilder_ {
	private static final File tempFile = new File("snappy_test.led");

	@Test
	public void should_build_led() {
		Led<TestTransaction> led = buildLed();
		led.iterator().forEachRemaining(t -> System.out.println(t.id() + " " + t.b()));
	}

	@Test
	public void should_build_and_write_led() {
		Led<TestTransaction> led = buildLed();
		new LedWriter(tempFile).write(led);
	}

	private Led<TestTransaction> buildLed() {
		LedBuilder<TestTransaction> builder = new LedBuilder<>(TestTransaction.SIZE, TestTransaction::new);
		for (int i = 10; i <= 1000; i += 5) {
			int id = (int) Math.cos(i / 20 * Math.PI) * i;
			builder.createTransaction().id(id).b(i - 500).f(i * 100.0 / 20.0);
		}
		Led<TestTransaction> led = builder.build();
		return led;
	}

	@After
	public void tearDown() {
		tempFile.delete();
		tempFile.getParentFile().delete();
	}
}
