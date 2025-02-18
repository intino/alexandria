package io.intino.alexandria.ollama.tools;

public class BadToolCallException extends RuntimeException {

	public BadToolCallException() {
	}

	public BadToolCallException(String message) {
		super(message);
	}

	public BadToolCallException(String message, Throwable cause) {
		super(message, cause);
	}
}
