package io.intino.alexandria.bpm;

public class State {
	private final String name;
	private final Task task;
	private final Type type;

	public State(String name, Task task) {
		this(name, task, Type.Normal);
	}

	public State(String name, Task task, Type type) {
		this.name = name;
		this.task = task;
		this.type = type;
	}

	public String name() {
		return name;
	}

	public Task task() {
		return task;
	}

	public Type type() {
		return type;
	}

	public enum Type {Initial, Normal, Terminal}
}
