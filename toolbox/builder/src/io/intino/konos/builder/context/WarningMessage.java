package io.intino.konos.builder.context;

import java.io.File;
import java.io.PrintWriter;

public class WarningMessage {
	private final String message;
	private int importance;
	private final File owner;
	private final int line;
	private final int column;

	public WarningMessage(String message, File owner, int line, int column) {
		this.importance = importance;
		this.message = message;
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

	public File owner() {
		return owner;
	}

	public String message() {
		return message;
	}

	public int line() {
		return this.line;
	}

	public int column() {
		return this.column;
	}
}
