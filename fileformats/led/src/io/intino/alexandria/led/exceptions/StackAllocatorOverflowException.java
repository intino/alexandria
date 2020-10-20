package io.intino.alexandria.led.exceptions;

public class StackAllocatorOverflowException extends RuntimeException {

	public StackAllocatorOverflowException() {
	}

	public StackAllocatorOverflowException(String message) {
		super(message);
	}
}
