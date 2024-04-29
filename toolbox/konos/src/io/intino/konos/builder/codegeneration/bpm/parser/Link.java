package io.intino.konos.builder.codegeneration.bpm.parser;


public class Link {
	private Type type;
	private boolean isDefault;
	private State target;
	private String description;

	public Type type() {
		return type;
	}

	public Link type(Type type) {
		this.type = type;
		return this;
	}

	public State state() {
		return target;
	}

	public Link to(State target) {
		this.target = target;
		return this;
	}

	public State to() {
		return target;
	}

	public Link description(String description) {
		this.description = description;
		return this;
	}

	public String description() {
		return description;
	}

	public Link isDefault(boolean isDefault) {
		this.isDefault = isDefault;
		return this;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public enum Type {
		Line, Exclusive, Inclusive
	}
}
