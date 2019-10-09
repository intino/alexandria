package io.intino.alexandria.ui.displays;

public class UserMessage implements java.io.Serializable {
	private String message;
	private Type type;

	public enum Type {
		Success, Error, Warning, Info, Loading
	}

	public String message() {
		return this.message;
	}

	public Type type() {
		return type;
	}

	public UserMessage message(String message) {
		this.message = message;
		return this;
	}

	public UserMessage type(Type type) {
		this.type = type;
		return this;
	}
}