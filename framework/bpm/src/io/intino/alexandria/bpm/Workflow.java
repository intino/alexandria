package io.intino.alexandria.bpm;

import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageHub;

import java.util.*;

import static io.intino.alexandria.bpm.Link.Type.Default;
import static io.intino.alexandria.bpm.Link.Type.Exclusive;
import static io.intino.alexandria.bpm.Task.Type.Automatic;


public class Workflow {

	private static final String Channel = "ProcessStatus";
	private MessageHub messageHub;
	private ProcessFactory processFactory;
	private Map<String, Process> processes = new HashMap<>();
	private Set<String> advancingProcesses = new HashSet<>();

	public Workflow(MessageHub messageHub, ProcessFactory processFactory) {
		this.messageHub = messageHub;
		this.messageHub.attachListener(Channel, Workflow.this::process);
		this.processFactory = processFactory;
	}

	private void process(Message message) {
		ProcessStatus status = new ProcessStatus(message);
		waitSemaphore(status);
		advancingProcesses.add(status.processId());
		if (startProcess(status)) initProcess(status);
		else if (terminatedProcess(status)) terminateProcess(status);
		else processes.get(status.processId()).register(status);
		if (stateExited(status)) {
			if (stateIsTerminal(status)) sendTerminationMessage(status);
			else advanceProcess(status);
		} else if (stateRejectedOrSkipped(status))
			propagateRejectionOnBranch(processes.get(status.processId()), stateOf(status));
		advancingProcesses.remove(status.processId());
	}

	private void initProcess(ProcessStatus status) {
		Process process = processFactory.createProcess(status.processId(), status.processName());
		processes.put(status.processId(), process);
		process.register(status);
		invoke(process, process.initialState());
	}

	private void terminateProcess(ProcessStatus status) {
		Process process = processes.get(status.processId());
		process.register(status);
		if(!process.hasCallback()) return;
		Process callbackProcess = processes.get(process.callbackProcess());
		messageHub.sendMessage(Channel, exitMessage(callbackProcess, callbackProcess.state(process.callbackState()), "Process " + process.id() + " exited"));
		//processes.remove(status.processId()); // TODO all states finished???
	}

	private void sendTerminationMessage(ProcessStatus status) {
		messageHub.sendMessage(Channel, terminateProcessMessage(processes.get(status.processId())));
	}

	private void advanceProcess(ProcessStatus status) {
		Process process = processes.get(status.processId());
		List<Link> links = process.linksOf(stateOf(status));
		if (links.isEmpty()) return;
		processLinks(process, links, typeOf(links));
	}

	private void waitSemaphore(ProcessStatus status) {
		while (advancingProcesses.contains(status.processId())) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void processLinks(Process process, List<Link> links, Link.Type type) {
		boolean invoked = false;
		for (Link link : links) {
			if ((type == Exclusive && invoked) || !stateAccept(process, link)) sendRejectionMessage(process, link);
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

	private boolean stateIsTerminal(ProcessStatus status) {
		return processes.get(status.processId()).state(stateOf(status)).isTerminal();
	}

	private boolean stateExited(ProcessStatus status) {
		return status.hasStateInfo() && status.stateInfo().status().equals("Exit");
	}

	private boolean stateRejectedOrSkipped(ProcessStatus status) {
		return status.hasStateInfo() &&
				(status.stateInfo().status().equals("Rejected") || status.stateInfo().status().equals("Skipped"));
	}

	private boolean startProcess(ProcessStatus status) {
		return status.processStatus().equals("Enter");
	}

	private boolean terminatedProcess(ProcessStatus status) {
		return status.processStatus().equals("Exit");
	}

	private Link defaultLink(List<Link> links) {
		return links.stream().filter(l -> l.type() == Default).findFirst().orElse(null);
	}

	private void invoke(Process process, State state) {
		new Thread(() -> {
			messageHub.sendMessage(Channel, enterMessage(process, state));
			String result = state.task().execute();
			if (state.task().type() == Automatic) messageHub.sendMessage(Channel, exitMessage(process, state, result));
		}).start();
	}

	private void sendRejectionMessage(Process process, Link link) {
		messageHub.sendMessage(Channel, rejectMessage(process, process.state(link.to())));
	}

	private void propagateRejectionOnBranch(Process process, String state) {
		List<Link> links = process.linksOf(state);
		for (Link link : links) {
			if (!process.predecessorsHaveFinished(link.to())) continue;
			if (anyPredecessorHasExited(process, link)) invoke(process, process.state(link.to()));
			else messageHub.sendMessage(Channel, skipMessage(process, process.state(link.to())));
		}
	}

	private boolean anyPredecessorHasExited(Process process, Link link) {
		return process.predecessorsFinishedStatus(link.to()).stream().anyMatch(s -> s.stateInfo().status().equals("Exit"));
	}

	private Message enterMessage(Process process, State state) {
		return stateMessage(process, state, "Enter").message();
	}

	private Message exitMessage(Process process, State state, String result) {
		ProcessStatus status = stateMessage(process, state, "Exit");
		status.addTaskInfo(result);
		return status.message();
	}

	private Message rejectMessage(Process process, State state) {
		return stateMessage(process, state, "Rejected").message();
	}

	private Message skipMessage(Process process, State state) {
		return stateMessage(process, state, "Skipped").message();
	}

	private ProcessStatus stateMessage(Process process, State state, String stateStatus) {
		ProcessStatus status = processMessage(process, "Running");
		status.addStateInfo(state.name(), stateStatus);
		return status;
	}

	private ProcessStatus processMessage(Process process, String processStatus) {
		return new ProcessStatus(process.id(), process.name(), processStatus);
	}

	private Link.Type typeOf(List<Link> links) {
		return links.stream().filter(l -> l.type() != Default).findFirst().get().type();
	}

	private Message terminateProcessMessage(Process process) {
		return new ProcessStatus(process.id(), process.name(), "Exit").message();
	}

	private String stateOf(ProcessStatus status) {
		return status.stateInfo().name();
	}

	public Process process(String processId) {
		return processes.get(processId);
	}
}
