package io.intino.alexandria.bpm;

import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.message.Message;

import java.time.Instant;

import static io.intino.alexandria.bpm.Workflow.EventType;

public class ProcessStatus extends MessageEvent {
	private static final String Owner = "owner";
	private static final String Task = "Task";
	private static final String State = "State";
	private static final String User = "user";
	private static final String Duration = "duration";
	private static final String Name = "name";
	private static final String Status = "status";
	private static final String Ts = "ts";
	private static final String SS = "ss";
	private static final String Id = "id";
	private static final String CallbackProcess = "callbackProcess";
	private static final String CallbackState = "callbackState";
	private static final String BPM_SS = "bpm";

	public ProcessStatus(String id, String name, Process.Status processStatus) {
		super(new Message(EventType)
				.set(SS, BPM_SS)
				.set(Ts, Instant.now().toString())
				.set(Id, id)
				.set(Name, name)
				.set(Status, processStatus.name()));
	}

	public ProcessStatus(String id, String name, Process.Status processStatus, String owner, String callbackProcess, String callbackState) {
		super(new Message(EventType)
				.set(SS, BPM_SS)
				.set(Ts, Instant.now().toString())
				.set(Id, id)
				.set(Name, name)
				.set(Status, processStatus.name())
				.set(Owner, owner)
				.set(CallbackProcess, callbackProcess)
				.set(CallbackState, callbackState));
	}

	public ProcessStatus(MessageEvent event) {
		this(event.toMessage());
	}

	public ProcessStatus(Message message) {
		super(message.set(SS, BPM_SS)); // TODO: check
	}

	public Instant ts() {
		return Instant.parse(super.toMessage().get("ts").data());
	}

	public String processId() {
		return toMessage().get(Id).data();
	}

	public String owner() {
		return toMessage().contains(Owner) ? toMessage().get(Owner).data() : null;
	}

	public String processName() {
		return toMessage().get(Name).data();
	}

	public String processStatus() {
		return toMessage().get(Status).data();
	}

	public boolean hasCallback() {
		return toMessage().contains(CallbackProcess);
	}

	public String callbackProcess() {
		return toMessage().get(CallbackProcess).data();
	}

	public String callbackState() {
		return toMessage().get(CallbackState).data();
	}

	public boolean hasStateInfo() {
		return !toMessage().components(State).isEmpty();
	}

	public ProcessStatus addStateInfo(String name, State.Status status) {
		toMessage().add(new Message(State).set(Name, name).set(Status, status.name()));
		return this;
	}

	public StateInfo stateInfo() {
		return hasStateInfo() ? new StateInfo(toMessage().components(State).get(0)) : null;
	}

	public boolean hasTaskInfo() {
		return !toMessage().components(Task).isEmpty();
	}

	public void addTaskInfo(String user, String duration) {
		toMessage().add(new Message(Task).set(User, user).set(Duration, duration));
	}

	public Message get() {
		return toMessage();
	}

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
