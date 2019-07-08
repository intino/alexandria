package io.intino.alexandria.bpm;


import io.intino.alexandria.inl.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.intino.alexandria.bpm.State.Type.Initial;
import static java.util.stream.Collectors.toList;

public abstract class Process {

	private final String id;
	protected List<Message> messages = new ArrayList<>();
	private List<Link> links = new ArrayList<>();
	private Map<String, State> states = new HashMap<>();

	protected Process(String id) {
		this.id = id;
	}

	protected void addState(State state) {
		states.put(state.name(), state);
	}

	protected void addLink(Link link) {
		links.add(link);
	}

	public void registerMessage(Message message) {
		messages.add(message);
	}

	public State state(String state) {
		return states.get(state);
	}

	public synchronized List<Link> linksOf(String state) {
		return links.stream().filter(l -> l.from().equals(state) && predecessorsHaveFinished(l.to())).collect(toList());
	}

	private boolean predecessorsHaveFinished(String state) {
		return links.stream().filter(l -> l.to().equals(state))
				.allMatch(l -> stateFinished(l.from()));
	}

	private boolean stateFinished(String stateName) {
		return messages.stream()
				.filter(m -> !m.components("ProcessStatus.State").isEmpty())
				.map(m -> m.components("ProcessStatus.State").get(0))
				.anyMatch(m -> m.get("name").data().equals(stateName) && m.get("status").data().equals("Exit"));
	}

	public State initialState() {
		return states.values().stream().filter(s -> s.type() == Initial).findFirst().get();
	}

	public String id() {
		return id;
	}

	public abstract String name();

	public List<Message> messages() {
		return messages;
	}
}
