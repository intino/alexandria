package io.intino.test.allocators;

import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.allocators.DefaultAllocator;
import io.intino.alexandria.led.allocators.SchemaAllocator;
import io.intino.alexandria.led.allocators.stack.StackAllocators;
import io.intino.alexandria.led.allocators.stack.StackListAllocator;
import io.intino.test.transactions.TestTransaction;

public class TestMemoryUsedByJVM {
	private static final int NUM_ELEMENTS = 20_000_000;
	private static final Runtime RUNTIME = Runtime.getRuntime();
	private static final float BYTES_TO_MB = 1024.0f * 1024.0f;

	public static void main(String[] args) {
		// testDefaultAllocator();
		testStackAllocator();
		// testStackListAllocator();

		//final long defaultAllocatorMemory = getMemoryUsed(new DefaultAllocator<>(TestSchemaObj.SIZE, TestSchemaObj::new));

		//final long stackAllocatorMemory = getMemoryUsed(StackAllocators.newManaged(TestSchemaObj.SIZE, NUM_ELEMENTS, TestSchemaObj::new));

		//final long stackListAllocatorMemory = getMemoryUsed(new StackListAllocator<>(NUM_ELEMENTS / 10, TestSchemaObj.SIZE, TestSchemaObj::new, StackAllocators::newManaged));


		//final float max = Math.max(Math.max(defaultAllocatorMemory, stackAllocatorMemory), stackListAllocatorMemory);

		//final float defaultAllocatorRelativeUsage = defaultAllocatorMemory / max * 100.0f;
		//final float stackAllocatorRelativeUsage = stackAllocatorMemory / max * 100.0f;
		//final float stackListAllocatorRelativeUsage = stackListAllocatorMemory / max * 100.0f;

		//System.out.println("Test Allocator Memory Used (" + NUM_ELEMENTS + " elements, element size in bytes = " + TestSchemaObj.SIZE + ")");
		//System.out.println(">> Off-Heap memory used: " + (NUM_ELEMENTS * TestSchemaObj.SIZE) / BYTES_TO_MB + " MB");
		//System.out.println();
		//System.out.println(">> DefaultAllocator: " + defaultAllocatorMemory / BYTES_TO_MB + " MB used (" + defaultAllocatorRelativeUsage + "%)");
		//System.out.println(">> StackAllocator: " + stackAllocatorMemory / BYTES_TO_MB + " MB used (" + stackAllocatorRelativeUsage + "%)");
		//System.out.println(">> StackListAllocator: " + stackListAllocatorMemory / BYTES_TO_MB + " MB used (" + stackListAllocatorRelativeUsage + "%)");
	}

	private static void testDefaultAllocator() {
		final long defaultAllocatorMemory = getMemoryUsed(new DefaultAllocator<>(TestTransaction.SIZE, TestTransaction::new));
		System.out.println("Test Allocator Memory Used (" + NUM_ELEMENTS + " elements, element size in bytes = " + TestTransaction.SIZE + ")");
		System.out.println(">> Off-Heap memory used: " + (NUM_ELEMENTS * TestTransaction.SIZE) / BYTES_TO_MB + " MB");
		System.out.println(">> DefaultAllocator: " + defaultAllocatorMemory / BYTES_TO_MB + " MB used");
	}

	private static void testStackAllocator() {
		final long stackAllocatorMemory = getMemoryUsed(StackAllocators.newManaged(TestTransaction.SIZE, NUM_ELEMENTS, TestTransaction::new));
		System.out.println("Test Allocator Memory Used (" + NUM_ELEMENTS + " elements, element size in bytes = " + TestTransaction.SIZE + ")");
		System.out.println(">> Off-Heap memory used: " + (NUM_ELEMENTS * TestTransaction.SIZE) / BYTES_TO_MB + " MB");
		System.out.println(">> StackAllocator: " + stackAllocatorMemory / BYTES_TO_MB + " MB used");
	}

	private static void testStackListAllocator() {
		final long stackListAllocatorMemory = getMemoryUsed(new StackListAllocator<>(NUM_ELEMENTS / 10, TestTransaction.SIZE, TestTransaction::new, StackAllocators::newManaged));
		System.out.println("Test Allocator Memory Used (" + NUM_ELEMENTS + " elements, element size in bytes = " + TestTransaction.SIZE + ")");
		System.out.println(">> Off-Heap memory used: " + (NUM_ELEMENTS * TestTransaction.SIZE) / BYTES_TO_MB + " MB");
		System.out.println(">> StackListAllocator: " + stackListAllocatorMemory / BYTES_TO_MB + " MB used");
	}

	private static long getMemoryUsed(SchemaAllocator<TestTransaction> allocator) {
		RUNTIME.gc();
		final long startMemory = usedMemory();
		Transaction schema = null;
		for (int i = 0; i < NUM_ELEMENTS; i++) schema = allocator.malloc();
		if (schema != null) schema.address();
		final long usedMemory = usedMemory() - startMemory;
		allocator.free();
		RUNTIME.gc();
		return usedMemory;
	}

	private static long usedMemory() {
		return RUNTIME.totalMemory() - RUNTIME.freeMemory();
	}
}
