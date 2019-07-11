package io.intino.alexandria.bpm;

public abstract class Task {
	private final Type type;

	Task(Type type) {
		this.type = type;
	}

	public boolean accept() {
		return true;
	}

	abstract String execute();

	Type type(){
		return type;
	}

	enum Type {Automatic, Manual, CallActivity}
}
