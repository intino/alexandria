package io.intino.alexandria.bpm;

import java.util.HashSet;
import java.util.Set;

import static io.intino.alexandria.bpm.State.Type.Initial;
import static io.intino.alexandria.bpm.State.Type.Terminal;
import static java.util.Arrays.asList;

public class State {
	private final String name;
	private final Task task;
	private final Set<Type> type;

	public State(String name, Task task) {
		this(name, task, Type.Normal);
	}

	public State(String name, Task task, Type... type) {
		this.name = name;
		this.task = task;
		this.type = new HashSet<>(asList(type));
	}

	public String name() {
		return name;
	}

	public Task task() {
		return task;
	}

	public boolean isInitial(){
		return type.contains(Initial);
	}

	public boolean isTerminal() {
		return type.contains(Terminal);
	}

	public enum Type {Initial, Normal, Terminal}
	public enum Status {Enter, Exit, Rejected, Skipped}
}
