package io.intino.alexandria.bpm;

public class Link {

	private final String from;
	private final String to;
	private final Type type;

	public enum Type {Exclusive, Inclusive, Default}

	public Link(String from, String to, Type type) {
		this.from = from;
		this.to = to;
		this.type = type;
	}

	public String from() {
		return from;
	}

	public String to() {
		return to;
	}

	public Type type() {
		return type;
	}
}
