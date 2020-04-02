package io.intino.konos.builder.context;

import java.io.File;
import java.io.PrintWriter;

public class WarningMessage {
	public static final int NONE = 0;
	public static final int LIKELY_ERRORS = 1;
	public static final int POSSIBLE_ERRORS = 2;
	public static final int PARANOIA = 3;
	private String message;
	private Object data;
	private int importance;
	private final File owner;
	private final int line;
	private final int column;

	public WarningMessage(int importance, String message, File owner, int line, int column) {
		this.importance = importance;
		this.message = message;
		this.owner = owner;
		this.line = line;
		this.column = column;
	}

	public WarningMessage(int importance, String message, Object data, File owner, int line, int column) {
		this.importance = importance;
		this.message = message;
		this.data = data;
		this.owner = owner;
		this.line = line;
		this.column = column;
	}

	public static boolean isRelevant(int actual, int limit) {
		return actual <= limit;
	}

	public boolean isRelevant(int importance) {
		return isRelevant(this.importance, importance);
	}

	public void write(PrintWriter writer) {
		writer.print("warning: ");
		writer.println(this.message);

	}

	public int line() {
		return this.line;
	}

	public int column() {
		return this.column;
	}
}
