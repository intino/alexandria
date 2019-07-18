package io.intino.alexandria.bpm;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public abstract class Process {

	private final String id;
	protected final List<ProcessStatus> processStatusList = new ArrayList<>();
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
//		int index = binarySearch(processStatusList, status) + 1;
		processStatusList.add(status);
	}

	public State state(String state) {
		return states.get(state);
	}

	public List<Link> linksOf(String state) {
		return links.stream().filter(l -> l.from().equals(state) && !stateCovered(l.to()) && predecessorsHaveFinished(l.to())).collect(toList());
	}

	public boolean hasCallback() {
		return processStatusList.get(0).hasCallback();
	}

	public String callbackProcess() {
		return processStatusList.get(0).callbackProcess();
	}

	public String callbackState() {
		return processStatusList.get(0).callbackState();
	}

	private boolean stateCovered(String state) {
		return processStatusList.stream().anyMatch(s -> s.hasStateInfo() && s.stateInfo().name().equals(state));
	}

	boolean predecessorsHaveFinished(String state) {
		return links.stream().filter(l -> l.to().equals(state))
				.allMatch(l -> stateFinished(l.from()));
	}

	protected List<ProcessStatus> predecessorsFinishedStatus(String state) {
		return links.stream().filter(l -> l.to().equals(state))
				.filter(l -> stateFinished(l.from()))
				.map(l -> exitStateStatus(l.from()))
				.collect(toList());
	}

	protected ProcessStatus exitStateStatus(String stateName) {
		return new ArrayList<>(processStatusList).stream()
				.filter(s -> s.hasStateInfo() && s.stateInfo().name().equals(stateName) && s.stateInfo().isTerminated())
				.findFirst().orElse(null);
	}

	private boolean stateFinished(String stateName) {
		return processStatusList.stream()
				.filter(ProcessStatus::hasStateInfo)
				.map(ProcessStatus::stateInfo)
				.anyMatch(s -> s.name().equals(stateName) && s.isTerminated());
	}

	public State initialState() {
		return states.values().stream().filter(State::isInitial).findFirst().get();
	}

	public String id() {
		return id;
	}

	protected String owner() {
		return processStatusList.get(0).owner();
	}

	public abstract String name();

	public List<ProcessStatus> messages() {
		return processStatusList;
	}

	public boolean isFinished() {
		String status = processStatusList.get(processStatusList.size() - 1).processStatus();
		return status.equals("Exit") || status.equals("Aborted");
	}

	void resume(List<ProcessStatus> statuses) {
		this.processStatusList.clear();
		this.processStatusList.addAll(statuses);
	}

	protected void onAbort(){}

	public String finishStatus() {
		return processStatusList.get(processStatusList.size() - 1).processStatus();
	}
}
