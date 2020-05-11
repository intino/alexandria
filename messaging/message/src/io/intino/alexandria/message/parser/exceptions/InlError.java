package io.intino.alexandria.message.parser.exceptions;

public class InlError {
	protected final int line;
	protected final int column;
	protected final String message;

	public InlError(int line, int column, String message) {
		this.line = line - 1;
		this.column = column;
		this.message = message;
	}

	public int line() {
		return line;
	}

	public int column() {
		return column;
	}

	public String message() {
		return message;
	}
}
