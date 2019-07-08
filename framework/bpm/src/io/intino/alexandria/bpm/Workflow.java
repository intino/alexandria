package io.intino.alexandria.bpm;

import io.intino.alexandria.inl.Message;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.intino.alexandria.bpm.Link.Type.Default;
import static io.intino.alexandria.bpm.Link.Type.Exclusive;
import static io.intino.alexandria.bpm.State.Type.Terminal;
import static io.intino.alexandria.bpm.Task.Type.Automatic;


public class Workflow {

	private static final String Channel = "ProcessStatus";
	private MessageHub messageHub;
	private ProcessFactory processFactory;
	private Map<String, Process> processes = new HashMap<>();

	public Workflow(MessageHub messageHub, ProcessFactory processFactory) {
		this.messageHub = messageHub;
		this.messageHub.registerListener(Channel, Workflow.this::process);
		this.processFactory = processFactory;
	}

	private void process(Message message) {
		if (startProcess(message)) initProcess(message);
		else processes.get(processId(message)).registerMessage(message);
		if (stateExited(message)) {
			if (stateIsTerminal(message)) terminateProcess(message);
			else advanceProcess(message);
		}
	}

	private void initProcess(Message message) {
		Process process = processFactory.createProcess(processId(message), processName(message));
		processes.put(processId(message), process);
		process.registerMessage(message);
		invoke(process, process.initialState());
	}

	private void terminateProcess(Message message) {
		messageHub.sendMessage(Channel, terminateProcessMessage(processes.get(processId(message))));
		processes.remove(processId(message));
	}

	private void advanceProcess(Message message) {
		Process process = processes.get(processId(message));
		List<Link> links = process.linksOf(stateOf(message));
		if (links.isEmpty()) return;
		processLinks(process, links, typeOf(links));
	}

	private void processLinks(Process process, List<Link> links, Link.Type type) {
		boolean invoked = false;
		for (Link link : links) {
			if ((type == Exclusive && invoked) || !stateAccept(process, link)) processRejection(process, link);
			else {
				invoked = true;
				invoke(process, process.state(link.to()));
			}
		}
		if (!invoked && defaultLink(links) != null) invoke(process, process.state(defaultLink(links).to()));
	}

	private boolean stateAccept(Process process, Link link) {
		return process.state(link.to()).task().accept();
	}

	private boolean stateIsTerminal(Message message) {
		return processes.get(processId(message)).state(stateOf(message)).type() == Terminal;
	}

	private String processId(Message message) {
		return message.get("id").data();
	}

	private String processName(Message message) {
		return message.get("name").data();
	}

	private boolean stateExited(Message message) {
		List<Message> components = message.components("ProcessStatus.State");
		return !components.isEmpty() && components.get(0).get("status").data().equals("Exit");
	}

	private boolean startProcess(Message message) {
		return message.get("status").data().equals("Enter");
	}

	private Link defaultLink(List<Link> links) {
		return links.stream().filter(l -> l.type() == Default).findFirst().orElse(null);
	}

	private void invoke(Process process, State state) {
		messageHub.sendMessage(Channel, enterMessage(process, state));
		new Thread(() -> {
			String result = state.task().execute();
			if (state.task().type() == Automatic) messageHub.sendMessage(Channel, exitMessage(process, state, result));
		}).start();
	}

	private void processRejection(Process process, Link link) {
		messageHub.sendMessage(Channel, rejectMessage(process, process.state(link.to())));
		propagateRejectionOnBranch(process, process.state(link.to())); // TODO
	}

	private void propagateRejectionOnBranch(Process process, State state) {
		//TODO
	}

	private Message enterMessage(Process process, State state) {
		return stateMessage(process, state, "Enter");
	}

	private Message exitMessage(Process process, State state, String result) {
		Message message = stateMessage(process, state, "Exit");
		message.components(Channel + ".State").get(0)
				.add(new Message(Channel + ".State.Task").set("result", result));
		return message;
	}

	private Message rejectMessage(Process process, State state) {
		return stateMessage(process, state, "Rejected");
	}

	private Message stateMessage(Process process, State state, String stateStatus) {
		Message message = processMessage(process, "Running");
		message.add(stateMessage(state, stateStatus));
		return message;
	}

	private Message processMessage(Process process, String processStatus) {
		return new Message(Channel)
				.set("ts", Instant.now().toString())
				.set("id", process.id())
				.set("name", process.name())
				.set("status", processStatus);
	}

	private Message stateMessage(State state, String stateStatus) {
		return new Message(Channel + ".State")
				.set("name", state.name())
				.set("status", stateStatus);
	}

	private Link.Type typeOf(List<Link> links) {
		return links.stream().filter(l -> l.type() != Default).findFirst().get().type();
	}

	private Message terminateProcessMessage(Process process) {
		return processMessage(process, "Exit");
	}

	private String stateOf(Message message) {
		return message.components(Channel + ".State").get(0).get("name").data();
	}

	public Process process(String processId) {
		return processes.get(processId);
	}
}
