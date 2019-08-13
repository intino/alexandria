package io.intino.alexandria.bpm;

import io.intino.alexandria.bpm.PersistenceManager.InMemoryPersistenceManager;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageHub;
import io.intino.alexandria.message.MessageReader;
import io.intino.alexandria.message.MessageWriter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;

import static io.intino.alexandria.bpm.Link.Type.Default;
import static io.intino.alexandria.bpm.Link.Type.Exclusive;
import static io.intino.alexandria.bpm.Process.Status.Running;
import static io.intino.alexandria.bpm.Task.Type.Automatic;
import static java.lang.Thread.sleep;
import static java.util.stream.Collectors.toList;


public class Workflow {
	public static final String Channel = "ProcessStatus";
	protected final MessageHub messageHub;
	private final PersistenceManager persistence;
	private ProcessFactory factory;
	private Map<String, Process> processes = new ConcurrentHashMap<>();
	private Set<String> advancingProcesses = new HashSet<>();

	public Workflow(MessageHub messageHub, ProcessFactory factory) {
		this(messageHub, factory, new InMemoryPersistenceManager());
	}

	public Workflow(MessageHub messageHub, ProcessFactory factory, PersistenceManager persistence) {
		this.messageHub = messageHub;
		this.persistence = persistence;
		this.factory = factory;
		loadActiveProcesses();
		this.messageHub.attachListener(Channel, Workflow.this::process);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			while (!advancingProcesses.isEmpty()) {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}));
	}

	private void loadActiveProcesses() {
		persistence.list("active/").forEach(path -> {
			List<ProcessStatus> statuses = messagesOf("active/" + path);
			ProcessStatus status = statuses.get(0);
			processes.put(status.processId(), factory.createProcess(status.processId(), status.processName()));
			process(status.processId()).resume(statuses);
		});
	}

	private void process(Message message) {
		ProcessStatus status = new ProcessStatus(message);
		waitSemaphore(status);
		getSemaphore(status);
		doProcess(status);
		releaseSemaphore(status);
	}

	private void doProcess(ProcessStatus status) {
		if (startProcess(status)) initProcess(status);
		Process process = processes.get(status.processId());
		if (process == null) addMessageToFinishedProcess(status);
		else doProcess(process, status);
	}

	private void doProcess(Process process, ProcessStatus status) {
		if (stateExited(status)) {
			if (!taskIsAutomatic(status)) process.register(status);
			if (stateIsTerminal(status)) registerTerminationMessage(status);
			else advanceProcess(status);
		} else if (stateRejectedOrSkipped(status)) propagateRejectionOnBranch(process, stateOf(status));
		persistProcess(process);
	}

	private void addMessageToFinishedProcess(ProcessStatus status) {
		List<ProcessStatus> statuses = messagesOf(finishedPathOf(status.processId()));
		if (statuses.isEmpty()) {
			Logger.error("Received status from non-existing process: " + status.processId());
			return;
		}
		statuses.add(status);
		write(finishedPathOf(status.processId()), statuses);
	}

	private List<ProcessStatus> messagesOf(String path) {
		return StreamSupport.stream(new MessageReader(persistence.read(path)).spliterator(), false)
				.map(ProcessStatus::new)
				.collect(toList());
	}

	private void getSemaphore(ProcessStatus status) {
		advancingProcesses.add(status.processId());
	}

	private void releaseSemaphore(ProcessStatus status) {
		advancingProcesses.remove(status.processId());
	}

	private void persistProcess(Process process) {
		if (process.isFinished()) {
			terminateProcess(process);
			persistence.delete(activePathOf(process.id()));
			write(finishedPathOf(process.id()), process.messages());
		} else write(activePathOf(process.id()), process.messages());
	}

	private void write(String path, List<ProcessStatus> messages) {
		try {
			MessageWriter writer = new MessageWriter(persistence.write(path));
			for (ProcessStatus message : new ArrayList<>(messages)) writer.write(message.message());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String activePathOf(String id) {
		return "active/" + id + ".process";
	}

	private String finishedPathOf(String id) {
		return "finished/" + id + ".process";
	}

	private void initProcess(ProcessStatus status) {
		Process process = factory.createProcess(status.processId(), status.processName());
		processes.put(status.processId(), process);
		process.register(status);
		invoke(process, process.initialState());
	}

	private void terminateProcess(Process process) {
		processes.remove(process.id());
		if (!process.hasCallback()) return;
		Process callbackProcess = processes.get(process.callbackProcess());
		if (callbackProcess == null) return;
		sendMessage(exitMessage(callbackProcess, callbackProcess.state(process.callbackState()), "Process " + process.id() + " has " + process.finishStatus()));
	}

	private void registerTerminationMessage(ProcessStatus status) {
		processes.get(status.processId()).register(terminateProcessMessage(processes.get(status.processId())));
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
				sleep(100);
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

	private boolean taskIsAutomatic(ProcessStatus status) {
		return processes.get(status.processId()).state(stateOf(status)).task().type() == Automatic;
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
		return status.processStatus().equals("Exit") || status.processStatus().equals("Aborted");
	}

	private Link defaultLink(List<Link> links) {
		return links.stream().filter(l -> l.type() == Default).findFirst().orElse(null);
	}

	private void invoke(Process process, State state) {
		new Thread(() -> {
			sendMessage(enterMessage(process, state));
			Task.Result result = state.task().execute();
			if (state.task().type() == Automatic) sendMessage(exitMessage(process, state, result.result()));
		}).start();
	}

	private void sendMessage(ProcessStatus status) {
		processes.get(status.processId()).register(status);
		messageHub.sendMessage(Channel, status.message());
	}

	private void sendRejectionMessage(Process process, Link link) {
		sendMessage(rejectMessage(process, process.state(link.to())));
	}

	private void propagateRejectionOnBranch(Process process, String state) {
		List<Link> links = process.linksOf(state);
		for (Link link : links) {
			if (!process.predecessorsHaveFinished(link.to())) continue;
			if (anyPredecessorHasExited(process, link)) invoke(process, process.state(link.to()));
			else sendMessage(skipMessage(process, process.state(link.to())));
		}
	}

	private boolean anyPredecessorHasExited(Process process, Link link) {
		return process.predecessorsFinishedStatus(link.to()).stream().anyMatch(s -> s.stateInfo().status().equals("Exit"));
	}

	private ProcessStatus enterMessage(Process process, State state) {
		return stateMessage(process, state, "Enter");
	}

	private ProcessStatus exitMessage(Process process, State state, String result) {
		ProcessStatus status = stateMessage(process, state, "Exit");
		status.addTaskInfo(new Task.Result(result));
		return status;
	}

	private ProcessStatus rejectMessage(Process process, State state) {
		return stateMessage(process, state, "Rejected");
	}

	private ProcessStatus skipMessage(Process process, State state) {
		return stateMessage(process, state, "Skipped");
	}

	private ProcessStatus stateMessage(Process process, State state, String stateStatus) {
		ProcessStatus status = processMessage(process, Running);
		status.addStateInfo(state.name(), State.Status.valueOf(stateStatus));
		return status;
	}

	private ProcessStatus processMessage(Process process, Process.Status processStatus) {
		return new ProcessStatus(process.id(), process.name(), processStatus);
	}

	private Link.Type typeOf(List<Link> links) {
		return links.stream().filter(l -> l.type() != Default).findFirst().get().type();
	}

	private ProcessStatus terminateProcessMessage(Process process) {
		return new ProcessStatus(process.id(), process.name(), Process.Status.valueOf("Exit"));
	}

	private String stateOf(ProcessStatus status) {
		return status.stateInfo().name();
	}

	public Process process(String processId) {
		return processes.get(processId);
	}
}
