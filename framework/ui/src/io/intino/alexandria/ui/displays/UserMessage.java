package io.intino.alexandria.ui.displays;

public class UserMessage implements java.io.Serializable {
	private String message;
	private Type type;
	private int autoHideDuration;

	public enum Type {
		Success, Error, Warning, Info, Loading
	}

	public String message() {
		return this.message;
	}

	public UserMessage message(String message) {
		this.message = message;
		return this;
	}

	public Type type() {
		return type;
	}

	public UserMessage type(Type type) {
		this.type = type;
		return this;
	}

	public int autoHideDuration() {
		return autoHideDuration;
	}

	public UserMessage autoHideDuration(int duration) {
		this.autoHideDuration = duration;
		return this;
	}
}