package io.intino.konos.builder.codegeneration.bpm.parser;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class State {

	private final String id;
	private final String label;
	private TaskType taskType;
	private final List<Type> types;
	private final List<Link> links;
	private String comment;

	public State(String id, String label) {
		this.id = id;
		this.label = label;
		this.links = new ArrayList<>();
		this.types = new ArrayList<>();
		this.taskType = TaskType.Default;
	}

	public String id() {
		return this.id;
	}

	public void addType(Type type) {
		this.types.add(type);
	}

	public boolean isInitial() {
		return types.contains(Type.Initial);
	}

	public boolean isTerminal() {
		return types.contains(Type.Terminal);
	}

	public List<Type> types() {
		return types;
	}

	public TaskType taskType() {
		return taskType;
	}

	public State taskType(TaskType taskType) {
		this.taskType = taskType;
		return this;
	}

	public State link(State state, Link.Type type, boolean isDefault) {
		links.add(new Link().to(state).type(type).isDefault(isDefault));
		return this;
	}


	public List<Link> links() {
		return links;
	}

	public Link links(String name) {
		return links.stream().filter(ec -> ec.state().id().equals(name)).findFirst().orElse(null);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		State state = (State) o;
		return Objects.equals(id, state.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return label + ":" + types;
	}

	public String label() {
		return label;
	}

	public String comment() {
		return comment;
	}

	public void comment(String comment) {
		this.comment = comment;
	}

	public enum Type {
		Initial, Intermediate, Terminal

	}

	public enum TaskType {
		Default, User, Manual, Service, Script, BusinessRule, Send, Receive, CallActivity;

	}
}
