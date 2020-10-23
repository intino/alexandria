package io.intino.test.allocators;

import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.allocators.DefaultAllocator;
import io.intino.alexandria.led.allocators.TransactionAllocator;
import io.intino.alexandria.led.allocators.indexed.ArrayAllocator;
import io.intino.alexandria.led.allocators.indexed.ListAllocator;
import io.intino.alexandria.led.allocators.stack.StackAllocators;
import io.intino.alexandria.led.allocators.stack.StackListAllocator;
import io.intino.test.transactions.TestTransaction;

public class TestAllocatorsSpeed {

	private static final int NUM_ELEMENTS = 20_000_000;
	private static final float MS_TO_SECONDS = 1000.0f;

	public static void main(String[] args) throws InterruptedException {

		// warmUp();

		final long ustackAllocatorTime = benchmark(StackAllocators.newUnmanaged(TestTransaction.SIZE, NUM_ELEMENTS, TestTransaction::new));

		final long stackAllocatorTime = benchmark(StackAllocators.newManaged(TestTransaction.SIZE, NUM_ELEMENTS, TestTransaction::new));

		final long stackListAllocatorTime = benchmark(new StackListAllocator<>(NUM_ELEMENTS / 10, TestTransaction.SIZE,
				TestTransaction::new, StackAllocators::newManaged));

		final long stackListAllocatorReservedTime = benchmark(new StackListAllocator<>(10, NUM_ELEMENTS / 10, TestTransaction.SIZE,
				TestTransaction::new, StackAllocators::newManaged));

		final long arrayAllocatorTime = benchmark(new ArrayAllocator<>(10, NUM_ELEMENTS / 10, TestTransaction.SIZE, TestTransaction::new));

		final long listAllocatorTime = benchmark(new ListAllocator<>(NUM_ELEMENTS / 10, TestTransaction.SIZE, TestTransaction::new));

		final long defaultAllocatorTime = benchmark(new DefaultAllocator<>(TestTransaction.SIZE, TestTransaction::new));


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


		System.out.println("Test Allocator Speed (" + NUM_ELEMENTS + " elements, element size in bytes = " + TestTransaction.SIZE + ")");
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
		final long ustackAllocatorTime = benchmark(n, StackAllocators.newUnmanaged(TestTransaction.SIZE, n, TestTransaction::new));
		final long stackAllocatorTime = benchmark(n, StackAllocators.newManaged(TestTransaction.SIZE, n, TestTransaction::new));
		final long stackListAllocatorTime = benchmark(n, new StackListAllocator<>(n / 10, TestTransaction.SIZE, TestTransaction::new, StackAllocators::newManaged));
		final long stackListAllocatorReservedTime = benchmark(n, new StackListAllocator<>(10, n / 10, TestTransaction.SIZE, TestTransaction::new, StackAllocators::newManaged));
		final long arrayAllocatorTime = benchmark(n, new ArrayAllocator<>(10, n / 10, TestTransaction.SIZE, TestTransaction::new));
		final long listAllocatorTime = benchmark(n, new ListAllocator<>(n / 10, TestTransaction.SIZE, TestTransaction::new));
		final long defaultAllocatorTime = benchmark(n, new DefaultAllocator<>(TestTransaction.SIZE, TestTransaction::new));
	}

	private static long benchmark(TransactionAllocator<TestTransaction> allocator) throws InterruptedException {
		return benchmark(NUM_ELEMENTS, allocator);
	}

	private static long benchmark(int n, TransactionAllocator<TestTransaction> allocator) throws InterruptedException {

		Runtime.getRuntime().gc();

		Thread.sleep(10);

		final long startTime = System.currentTimeMillis();

		Transaction schema = null;

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
