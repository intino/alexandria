package io.intino.test.io;

import io.intino.alexandria.led.*;
import io.intino.alexandria.led.allocators.SchemaAllocator;
import io.intino.alexandria.led.allocators.indexed.ListAllocator;
import io.intino.alexandria.led.leds.ListLed;
import io.intino.test.schemas.TestSchemaObj;
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
import static io.intino.test.schemas.TestSchemaObj.*;
import static org.junit.Assert.assertEquals;

public class WriterTest {
	private static final int NUM_ELEMENTS = 1_000_000;
	private static final File tempFile = new File("temp/snappy_test.led");
	private static final Random RANDOM = new Random();
	private static final AtomicLong RPU = new AtomicLong();

	@Test
	public void should_write_and_read() {
		List<TestSchemaObj> original = generateTestSchemaObjs(
				new ListAllocator<>(NUM_ELEMENTS / 10, TestSchemaObj.SIZE, TestSchemaObj::new))
				.collect(Collectors.toList());
		write(original);
		read(original);
	}

	private void write(List<TestSchemaObj> original) {
		long start;
		original.sort(Comparator.comparingLong(Schema::id));
		Led<TestSchemaObj> led = new ListLed<>(original);
		start = System.currentTimeMillis();
		new LedWriter(tempFile).write(led);
		System.out.println(">> Serialized " + NUM_ELEMENTS + "(" + TestSchemaObj.SIZE + " bytes each) in " + (System.currentTimeMillis() - start) / 1000 + " seconds");
	}

	private void read(List<TestSchemaObj> original) {
		long start;
		start = System.currentTimeMillis();
		LedStream<TestSchemaObj> led = new LedReader(tempFile).read(TestSchemaObj::new);
		System.out.println(">> Deserialized " + NUM_ELEMENTS + "(" + TestSchemaObj.SIZE + " bytes each) in " + (System.currentTimeMillis() - start) / 1000 + " seconds");
		AtomicInteger size = new AtomicInteger();
		IntStream.range(0, original.size()).forEach(i -> {
			assertEquals(original.get(i), led.next());
			size.getAndIncrement();
		});
		assertEquals(original.size(), size.get());
	}

	private Stream<TestSchemaObj> generateTestSchemaObjs(SchemaAllocator<TestSchemaObj> allocator) {
		Stream<TestSchemaObj> stream = IntStream.range(0, NUM_ELEMENTS)
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