package io.intino.test;

import io.intino.alexandria.led.*;
import io.intino.alexandria.led.allocators.TransactionAllocator;
import io.intino.alexandria.led.allocators.indexed.ListAllocator;
import io.intino.alexandria.led.leds.IteratorLedStream;
import io.intino.alexandria.led.leds.ListLed;
import io.intino.test.transactions.TestTransaction;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.intino.alexandria.led.util.BitUtils.maxPossibleNumber;
import static io.intino.test.transactions.TestTransaction.*;
import static org.junit.Assert.assertEquals;

public class LedWriter_ {
	private static final int NUM_ELEMENTS = 10_000_000;
	private static final File tempFile = new File("temp/snappy_test.led");
	private static final Random RANDOM = new Random();
	private static final AtomicLong RPU = new AtomicLong();

	@Test
	public void should_write_and_read() {
		List<TestTransaction> original = generateTestSchemaObjs(
				new ListAllocator<>(NUM_ELEMENTS / 10, TestTransaction.SIZE, TestTransaction::new))
				.collect(Collectors.toList());
		original.sort(Comparator.comparingLong(TestTransaction::id));
		write(original);
		read(original);
	}

	private void write(List<TestTransaction> original) {
		long start;
		LedStream<TestTransaction> led = new IteratorLedStream<>(SIZE, original.iterator());
		start = System.currentTimeMillis();
		new LedWriter(tempFile).write(Led.fromLedStream(led));
		System.out.println(">> Serialized " + NUM_ELEMENTS + "(" + TestTransaction.SIZE + " bytes each) in " + (System.currentTimeMillis() - start) / 1000 + " seconds");
	}

	private void read(List<TestTransaction> original) {
		LedReader reader = new LedReader(tempFile);
		assertEquals(original.size(), reader.size());
		long start;
		start = System.currentTimeMillis();
		Led<TestTransaction> led = reader.readAll(TestTransaction::new);
		System.out.println(">> Deserialized " + NUM_ELEMENTS + "(" + TestTransaction.SIZE + " bytes each) in " + (System.currentTimeMillis() - start) / 1000 + " seconds");
		AtomicInteger size = new AtomicInteger();
		IntStream.range(0, original.size()).forEach(i -> {
			assertEquals(original.get(i), led.transaction(i));
			size.getAndIncrement();
		});
		assertEquals(original.size(), size.get());
	}

	private Stream<TestTransaction> generateTestSchemaObjs(TransactionAllocator<TestTransaction> allocator) {
		Stream<TestTransaction> stream = IntStream.range(0, NUM_ELEMENTS)
				.parallel()
				.unordered()
				.mapToObj(i -> allocator.malloc()
						.id(RPU.incrementAndGet())
						.a((short) RANDOM.nextInt((int) maxPossibleNumber(A_BITS)))
						.b(RANDOM.nextInt((int) maxPossibleNumber(B_BITS)))
						.c(RANDOM.nextFloat() * RANDOM.nextInt())
						.d(RANDOM.nextInt((int) maxPossibleNumber(D_BITS)))
						.e(RANDOM.nextInt((int) maxPossibleNumber(E_BITS)))
						.f(RANDOM.nextDouble() * RANDOM.nextInt())
						.g((byte) RANDOM.nextInt((int) maxPossibleNumber(G_BITS)))
				);
		return stream.onClose(allocator::clear);
	}

	@After
	public void tearDown() {
		tempFile.delete();
	}
}