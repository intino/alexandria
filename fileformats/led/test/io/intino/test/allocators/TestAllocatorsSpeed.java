package io.intino.test.allocators;

import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.allocators.DefaultAllocator;
import io.intino.alexandria.led.allocators.SchemaAllocator;
import io.intino.alexandria.led.allocators.indexed.ArrayAllocator;
import io.intino.alexandria.led.allocators.indexed.ListAllocator;
import io.intino.alexandria.led.allocators.stack.StackAllocators;
import io.intino.alexandria.led.allocators.stack.StackListAllocator;
import io.intino.test.schemas.TestSchemaObj;

public class TestAllocatorsSpeed {

	private static final int NUM_ELEMENTS = 20_000_000;
	private static final float MS_TO_SECONDS = 1000.0f;

	public static void main(String[] args) throws InterruptedException {

		// warmUp();

		final long ustackAllocatorTime = benchmark(StackAllocators.newUnmanaged(TestSchemaObj.SIZE, NUM_ELEMENTS, TestSchemaObj::new));

		final long stackAllocatorTime = benchmark(StackAllocators.newManaged(TestSchemaObj.SIZE, NUM_ELEMENTS, TestSchemaObj::new));

		final long stackListAllocatorTime = benchmark(new StackListAllocator<>(NUM_ELEMENTS / 10, TestSchemaObj.SIZE,
				TestSchemaObj::new, StackAllocators::newManaged));

		final long stackListAllocatorReservedTime = benchmark(new StackListAllocator<>(10, NUM_ELEMENTS / 10, TestSchemaObj.SIZE,
				TestSchemaObj::new, StackAllocators::newManaged));

		final long arrayAllocatorTime = benchmark(new ArrayAllocator<>(10, NUM_ELEMENTS / 10, TestSchemaObj.SIZE, TestSchemaObj::new));

		final long listAllocatorTime = benchmark(new ListAllocator<>(NUM_ELEMENTS / 10, TestSchemaObj.SIZE, TestSchemaObj::new));

		final long defaultAllocatorTime = benchmark(new DefaultAllocator<>(TestSchemaObj.SIZE, TestSchemaObj::new));


		float max = Math.max(Math.max(defaultAllocatorTime, stackAllocatorTime), stackListAllocatorTime);
		max = Math.max(max, listAllocatorTime);
		max = Math.max(max, arrayAllocatorTime);
		max = Math.max(max, ustackAllocatorTime);


		final float defaultAllocatorRelativeTime = 100 - (defaultAllocatorTime / max * 100.0f);
		final float stackAllocatorRelativeTime = 100 - (stackAllocatorTime / max * 100.0f);
		final float ustackAllocatorRelativeTime = 100 - (ustackAllocatorTime / max * 100.0f);
		final float stackListAllocatorRelativeTime = 100 - (stackListAllocatorTime / max * 100.0f);
		final float stackListAllocatorReservedRelativeTime = 100 - (stackListAllocatorReservedTime / max * 100.0f);
		final float listAllocatorRelativeTime = 100 - (listAllocatorTime / max * 100.0f);
		final float arrayAllocatorRelativeTime = 100 - (arrayAllocatorTime / max * 100.0f);


		System.out.println("Test Allocator Speed (" + NUM_ELEMENTS + " elements, element size in bytes = " + TestSchemaObj.SIZE + ")");
		System.out.println(">> DefaultAllocator: " + defaultAllocatorTime / MS_TO_SECONDS + " seconds (" + defaultAllocatorRelativeTime + "% faster)");
		System.out.println(">> Managed StackAllocator: " + stackAllocatorTime / MS_TO_SECONDS + " seconds (" + stackAllocatorRelativeTime + "% faster)");
		System.out.println(">> Unmanaged StackAllocator: " + ustackAllocatorTime / MS_TO_SECONDS + " seconds (" + ustackAllocatorRelativeTime + "% faster)");
		System.out.println(">> StackListAllocator: " + stackListAllocatorTime / MS_TO_SECONDS + " seconds (" + stackListAllocatorRelativeTime + "% faster)");
		System.out.println(">> StackListAllocator (pre-allocated stacks): " + stackListAllocatorReservedTime / MS_TO_SECONDS + " seconds (" + stackListAllocatorReservedRelativeTime + "% faster)");
		System.out.println(">> ListAllocator: " + listAllocatorTime / MS_TO_SECONDS + " seconds (" + listAllocatorRelativeTime + "% faster)");
		System.out.println(">> ArrayAllocator: " + arrayAllocatorTime / MS_TO_SECONDS + " seconds (" + arrayAllocatorRelativeTime + "% faster)");
	}

	private static void warmUp() throws InterruptedException {

		final int n = NUM_ELEMENTS / 10_000;

		final long ustackAllocatorTime = benchmark(n, StackAllocators.newUnmanaged(TestSchemaObj.SIZE, n,
				TestSchemaObj::new));

		final long stackAllocatorTime = benchmark(n, StackAllocators.newManaged(TestSchemaObj.SIZE, n, TestSchemaObj::new));

		final long stackListAllocatorTime = benchmark(n, new StackListAllocator<>(n / 10, TestSchemaObj.SIZE,
				TestSchemaObj::new, StackAllocators::newManaged));

		final long stackListAllocatorReservedTime = benchmark(n, new StackListAllocator<>(10, n / 10, TestSchemaObj.SIZE,
				TestSchemaObj::new, StackAllocators::newManaged));

		final long arrayAllocatorTime = benchmark(n, new ArrayAllocator<>(10, n / 10, TestSchemaObj.SIZE, TestSchemaObj::new));

		final long listAllocatorTime = benchmark(n, new ListAllocator<>(n / 10, TestSchemaObj.SIZE, TestSchemaObj::new));

		final long defaultAllocatorTime = benchmark(n, new DefaultAllocator<>(TestSchemaObj.SIZE, TestSchemaObj::new));
	}

	private static long benchmark(SchemaAllocator<TestSchemaObj> allocator) throws InterruptedException {
		return benchmark(NUM_ELEMENTS, allocator);
	}

	private static long benchmark(int n, SchemaAllocator<TestSchemaObj> allocator) throws InterruptedException {

		Runtime.getRuntime().gc();

		Thread.sleep(10);

		final long startTime = System.currentTimeMillis();

		Schema schema = null;

		for (int i = 0; i < n; i++) {
			schema = allocator.malloc();
		}

		if (schema != null) {
			schema.address();
		}

		final long time = System.currentTimeMillis() - startTime;

		allocator.free();

		return time;
	}

}
