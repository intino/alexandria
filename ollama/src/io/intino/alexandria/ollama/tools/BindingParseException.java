package io.intino.alexandria.ollama.tools;

public class BindingParseException extends Exception {

	public BindingParseException() {
	}

	public BindingParseException(String message) {
		super(message);
	}

	public BindingParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BindingParseException(Throwable cause) {
		super(cause);
	}
}
