package io.intino.alexandria.led.exceptions;

public class IllegalMemoryOperationException extends RuntimeException {

	public IllegalMemoryOperationException() {
	}

	public IllegalMemoryOperationException(String message) {
		super(message);
	}

	public IllegalMemoryOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalMemoryOperationException(Throwable cause) {
		super(cause);
	}

	public IllegalMemoryOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
