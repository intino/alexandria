package io.intino.konos.builder;

public class KonosException extends Exception {
	private final boolean fatal;

	public KonosException() {
		fatal = true;
	}

	public KonosException(String message) {
		super(message);
		fatal = true;
	}

	public KonosException(String message, Throwable cause) {
		super(message, cause);
		fatal = true;
	}

	public KonosException(boolean fatal) {
		this.fatal = fatal;
	}

	public KonosException(String message, boolean fatal) {
		super(message);
		this.fatal = fatal;

	}

	public boolean isFatal() {
		return this.fatal;
	}

}
