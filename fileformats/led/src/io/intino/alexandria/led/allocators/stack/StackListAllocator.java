package io.intino.alexandria.led.allocators.stack;

import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.exceptions.StackAllocatorUnderflowException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class StackListAllocator<T extends Transaction> implements StackAllocator<T> {

	private static final int DEFAULT_INITIAL_STACK_COUNT = 1;


	private final List<StackAllocator<T>> stackAllocators;
	private final AtomicInteger currentStackAllocator;
	private final int elementsPerStack;
	private final int elementSize;
	private final TransactionFactory<T> schemaFactory;
	private final StackAllocatorFactory<T> stackAllocatorFactory;

	public StackListAllocator(int initialStackCount, int elementsPerStack, int elementSize,
							  TransactionFactory<T> schemaFactory, StackAllocatorFactory<T> stackAllocatorFactory) {

		this.stackAllocators = new ArrayList<>();
		currentStackAllocator = new AtomicInteger(0);
		this.elementsPerStack = elementsPerStack;
		this.elementSize = elementSize;
		this.schemaFactory = schemaFactory;
		this.stackAllocatorFactory = stackAllocatorFactory;

		reserve(initialStackCount);
	}

	public StackListAllocator(int elementsPerStack, int elementSize,
							  TransactionFactory<T> schemaFactory, StackAllocatorFactory<T> stackAllocatorFactory) {

		this(DEFAULT_INITIAL_STACK_COUNT, elementsPerStack, elementSize, schemaFactory, stackAllocatorFactory);
	}

	@Override
	public long stackPointer() {
		return currentStackAllocator().stackPointer();
	}

	@Override
	public long remainingBytes() {
		return currentStackAllocator().remainingBytes();
	}

	@Override
	public synchronized T malloc() {
		if (stackAllocators.isEmpty()) {
			allocateNewStack();
		} else if (remainingBytes() == 0) {
			int current = currentStackAllocator.incrementAndGet();
			if (current == stackAllocators.size()) {
				allocateNewStack();
			}
		}
		return currentStackAllocator().malloc();
	}

	@Override
	public synchronized T calloc() {
		if (stackAllocators.isEmpty()) {
			allocateNewStack();
		} else if (remainingBytes() == 0) {
			int current = currentStackAllocator.incrementAndGet();
			if (current == stackAllocators.size()) {
				allocateNewStack();
			}
		}
		return currentStackAllocator().calloc();
	}

	@Override
	public int transactionSize() {
		return elementSize;
	}

	@Override
	public synchronized void pop() {
		if (stackPointer() == 0) {
			if (currentStackAllocator.get() == 0) {
				throw new StackAllocatorUnderflowException();
			}
			currentStackAllocator.decrementAndGet();
		}
		currentStackAllocator().pop();
	}

	public void reserve(int stackCount) {
		for (int i = 0; i < stackCount; i++) {
			allocateNewStack();
		}
	}

	@Override
	public synchronized void clear() {
		stackAllocators.forEach(StackAllocator::clear);
		currentStackAllocator.set(0);
	}

	@Override
	public synchronized void free() {
		stackAllocators.forEach(StackAllocator::free);
		stackAllocators.clear();
		currentStackAllocator.set(0);
	}

	@Override
	public long address() {
		return currentStackAllocator().address();
	}

	@Override
	public long stackSize() {
		return currentStackAllocator().stackSize();
	}

	private void allocateNewStack() {
		stackAllocators.add(stackAllocatorFactory.create(elementSize, elementsPerStack, schemaFactory));
	}

	private StackAllocator<T> currentStackAllocator() {
		if (stackAllocators.isEmpty()) {
			allocateNewStack();
		}
		return stackAllocators.get(currentStackAllocator.get());
	}
}
