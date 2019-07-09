package io.intino.alexandria.bpm;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.intino.alexandria.bpm.State.Type.Initial;
import static java.util.stream.Collectors.toList;

public abstract class Process {

	private final String id;
	protected List<ProcessStatus> processStatusList = new ArrayList<>();
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

	public void register(ProcessStatus status) {
		processStatusList.add(status);
	}

	public State state(String state) {
		return states.get(state);
	}

	public List<Link> linksOf(String state) {
		return links.stream().filter(l -> l.from().equals(state) && !stateCovered(l.to()) && predecessorsHaveFinished(l.to())).collect(toList());
	}

	private boolean stateCovered(String state) {
		return processStatusList.stream().anyMatch(s -> s.hasStateInfo() && s.stateInfo().name().equals(state));
	}

	private boolean predecessorsHaveFinished(String state) {
		return links.stream().filter(l -> l.to().equals(state))
				.allMatch(l -> stateFinished(l.from()));
	}

	private boolean stateFinished(String stateName) {
		return processStatusList.stream()
				.filter(ProcessStatus::hasStateInfo)
				.map(ProcessStatus::stateInfo)
				.anyMatch(s -> s.name().equals(stateName) && s.status().equals("Exit"));
	}

	public State initialState() {
		return states.values().stream().filter(s -> s.type() == Initial).findFirst().get();
	}

	public String id() {
		return id;
	}

	public abstract String name();

	public List<ProcessStatus> messages() {
		return processStatusList;
	}
}
