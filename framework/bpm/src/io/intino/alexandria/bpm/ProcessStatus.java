package io.intino.alexandria.bpm;

import io.intino.alexandria.event.Event;
import io.intino.alexandria.message.Message;

import java.time.Instant;

import static io.intino.alexandria.bpm.Workflow.EventType;

public class ProcessStatus extends Event implements Comparable<ProcessStatus> {

	private static final String Owner = "owner";
	private static final String Task = "Task";
	private static final String State = "State";
	private static final String User = "user";
	private static final String Duration = "duration";
	private static final String Name = "name";
	private static final String Status = "status";
	private static final String Ts = "ts";
	private static final String Id = "id";
	private static final String CallbackProcess = "callbackProcess";
	private static final String CallbackState = "callbackState";

	public ProcessStatus(String id, String name, Process.Status processStatus) {
		super(new Message(EventType)
				.set(Ts, Instant.now().toString())
				.set(Id, id)
				.set(Name, name)
				.set(Status, processStatus.name()));
	}

	public ProcessStatus(String id, String name, Process.Status processStatus, String owner, String callbackProcess, String callbackState) {
		super(new Message(EventType)
				.set(Ts, Instant.now().toString())
				.set(Id, id)
				.set(Name, name)
				.set(Status, processStatus.name())
				.set(Owner, owner)
				.set(CallbackProcess, callbackProcess)
				.set(CallbackState, callbackState));
	}

	public ProcessStatus(Event event) {
		super(event.toMessage());
	}

	public ProcessStatus(Message message) {
		super(message);
	}

	public Instant ts() {
		return Instant.parse(message.get("ts").data());
	}

	public String processId() {
		return message.get(Id).data();
	}

	public String owner() {
		return message.contains(Owner) ? message.get(Owner).data() : null;
	}

	public String processName() {
		return message.get(Name).data();
	}

	public String processStatus() {
		return message.get(Status).data();
	}

	public boolean hasCallback() {
		return message.contains(CallbackProcess);
	}

	public String callbackProcess() {
		return message.get(CallbackProcess).data();
	}

	public String callbackState() {
		return message.get(CallbackState).data();
	}

	public boolean hasStateInfo() {
		return !message.components(State).isEmpty();
	}

	public ProcessStatus addStateInfo(String name, State.Status status) {
		message.add(new Message(State).set(Name, name).set(Status, status.name()));
		return this;
	}

	public StateInfo stateInfo() {
		return hasStateInfo() ? new StateInfo(message.components(State).get(0)) : null;
	}

	public boolean hasTaskInfo() {
		return !message.components(Task).isEmpty();
	}

	public void addTaskInfo(String user, String duration) {
		message.add(new Message(Task).set(User, user).set(Duration, duration));
	}

	public Message get() {
		return this.message;
	}

	@Override
	public int compareTo(ProcessStatus o) {
		return !ts().equals(o.ts()) ? ts().compareTo(o.ts()) : -1;
	}

	public static class StateInfo {

		private final String name;
		private final String status;

		private StateInfo(Message state) {
			this.name = state.get(Name).data();
			this.status = state.get(Status).data();
		}

		public String name() {
			return name;
		}

		public String status() {
			return status;
		}

		public boolean isTerminated() {
			return status.equals("Exit") || status.equals("Rejected") || status.equals("Skipped");
		}
	}

}
