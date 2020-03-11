package io.intino.alexandria.bpm;

public abstract class Task {
	private final Type type;

	public Task(Type type) {
		this.type = type;
	}

	public boolean accept() {
		return true;
	}

	public abstract void execute();

	Type type() {
		return type;
	}

	public enum Type {
		Default, User, Manual, Service, Script, BusinessRule, Send, Receive, CallActivity;

		public boolean isSynchronous() {
			return this == Default || this == Send || this == Script || this == BusinessRule;
		}
	}
}
