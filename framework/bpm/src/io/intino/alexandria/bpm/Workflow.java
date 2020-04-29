package io.intino.alexandria.bpm;

import io.intino.alexandria.bpm.PersistenceManager.InMemoryPersistenceManager;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageReader;
import io.intino.alexandria.message.MessageWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;

import static io.intino.alexandria.bpm.Link.Type.Default;
import static io.intino.alexandria.bpm.Link.Type.Exclusive;
import static io.intino.alexandria.bpm.Process.Status.Enter;
import static io.intino.alexandria.bpm.Process.Status.Running;
import static java.lang.Thread.sleep;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;


public abstract class Workflow {
	public static final String EventType = "ProcessStatus";
	private final PersistenceManager persistence;
	private ProcessFactory factory;
	private Map<String, Process> processes = new ConcurrentHashMap<>();

	public Workflow(ProcessFactory factory) {
		this(factory, new InMemoryPersistenceManager());
	}

	public Workflow(ProcessFactory factory, PersistenceManager persistence) {
		this.persistence = persistence;
		this.factory = factory;
		loadActiveProcesses();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			while (processes.values().stream().anyMatch(Process::isBusy)) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					Logger.error(e);
				}
			}
		}));
	}

	public List<Process> processes() {
		return new ArrayList<>(processes.values());
	}

	public void exitState(String processId, String processName, String stateName) {
		send(new ProcessStatus(processId, processName, Running)
				.addStateInfo(stateName, State.Status.Exit));
	}

	private void loadActiveProcesses() {
		long loaded = 0;
		List<BpmViewer.ProcessInfo> activeProcesses = new BpmViewer(persistence).activeProcesses();
		for (BpmViewer.ProcessInfo activeProcess : activeProcesses)
			try {
				loadProcess(activeProcess);
				loaded++;
			} catch (Throwable e) {
				Logger.error("Process at " + activeProcess.processPath() + " failed when loading.", e);
			}
		Logger.info("Number of active processes: " + activeProcesses.size() + ". Number of processes loaded: " + loaded);
	}

	private void loadProcess(BpmViewer.ProcessInfo activeProcess) {
		List<ProcessStatus> statuses = activeProcess.processStatuses();
		ProcessStatus status = statuses.get(0);
		processes.put(status.processId(), factory.createProcess(status.processId(), status.processName()));
		process(status.processId()).resume(statuses, activeProcess.data());
	}

	private Map<String, String> dataOf(String path) {
		Message message = new MessageReader(persistence.read(dataPath(path))).next();
		return message.attributes().stream().collect(toMap(a -> a, a -> message.get(a).asString()));
	}

	private void process(ProcessStatus status) {
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
			if (!taskIsSynchronous(status)) process.register(status);
			if (stateIsTerminal(status)) registerTerminationMessage(status);
			else advanceProcess(status);
		} else if (stateRejectedOrSkipped(status)) propagateRejectionOnBranch(process, stateOf(status));
		persistProcess(process);
	}

	private void addMessageToFinishedProcess(ProcessStatus status) {
		String finishedPath = finishedPathOf(status.processId());
		if (finishedPath == null) {
			Logger.error("Received status from non-existing process: " + status.processId());
			return;
		}
		List<ProcessStatus> statuses = messagesOf(finishedPath);
		statuses.add(status);
		write(finishedPath, statuses, null);
	}

	private List<ProcessStatus> messagesOf(String path) {
		return StreamSupport.stream(new MessageReader(persistence.read(path)).spliterator(), false)
				.map(ProcessStatus::new)
				.collect(toList());
	}

	private void releaseSemaphore(ProcessStatus status) {
		Process process = process(status.processId());
		if (process == null) return;
		if (process.isBusy()) process.release();
	}

	private void persistProcess(Process process) {
		if (process.isFinished()) {
			terminateProcess(process);
			persistence.delete(activePathOf(process.id()));
			persistence.delete(dataPath(activePathOf(process.id())));
			write(finishedPathOf(process), process);
		} else write(activePathOf(process.id()), process);
	}

	private void write(String path, Process process) {
		write(path, new ArrayList<>(process.messages()), new HashMap<>(process.data()));
	}

	private void write(String path, List<ProcessStatus> messages, Map<String, String> data) {
		try {
			MessageWriter writer = new MessageWriter(persistence.write(path));
			for (ProcessStatus message : new ArrayList<>(messages)) writer.write(message.get());
			writer.close();

			if (data == null) return;
			PrintWriter p = new PrintWriter(persistence.write(dataPath(path)));
			Message message = new Message("Data");
			data.forEach(message::set);
			p.print(message.toString());
			p.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private String dataPath(String path) {
		return path.replace(".process", ".data");
	}

	private String activePathOf(String id) {
		return new BpmViewer(persistence).activePathOf(id);
	}

	private String finishedPathOf(Process process) {
		return new BpmViewer(persistence).finishedPathOf(process.timetag(), process.id());
	}

	private String finishedPathOf(String processId) {
		return new BpmViewer(persistence).processInfo(processId).processPath();
	}

	public PersistenceManager persistence() {
		return persistence;
	}

	private void initProcess(ProcessStatus status) {
		Process process = factory.createProcess(status.processId(), status.processName());
		processes.put(status.processId(), process);
		process.register(status);
		invoke(process, process.initialState());
	}

	public void registerProcess(Process process) {
		processes.put(process.id(), process);
		process.register(new ProcessStatus(process.id(), process.name(), Enter));
		invoke(process, process.initialState());
	}

	private void terminateProcess(Process process) {
		processes.remove(process.id());
		if (!process.hasCallback()) return;
		Process callbackProcess = processes.get(process.callbackProcess());
		if (callbackProcess == null) return;
		sendMessage(exitMessage(callbackProcess, callbackProcess.state(process.callbackState())));
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

	private void getSemaphore(ProcessStatus status) {
		try {
			Process process = process(status.processId());
			if (process == null) return;
			process.acquire();
		} catch (InterruptedException e) {
			Logger.error(e);
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

	private boolean taskIsSynchronous(ProcessStatus status) {
		return processes.get(status.processId()).state(stateOf(status)).task().type().isSynchronous();
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
			state.task().execute();
			if (state.task().type().isSynchronous()) sendMessage(exitMessage(process, state));
		}).start();
	}

	private void sendMessage(ProcessStatus status) {
		processes.get(status.processId()).register(status);
		send(status);
	}

	public void receive(ProcessStatus processStatus) {
		process(processStatus);
	}

	public abstract void send(ProcessStatus processStatus);

	private void sendRejectionMessage(Process process, Link link) {
		new Thread(() -> sendMessage(rejectMessage(process, process.state(link.to())))).start();
	}

	private void propagateRejectionOnBranch(Process process, String state) {
		List<Link> links = process.linksOf(state);
		for (Link link : links) {
			if (!process.predecessorsHaveFinished(link.to())) continue;
			if (anyPredecessorHasExited(process, link)) invoke(process, process.state(link.to()));
			else sendSkipMessage(process, link);
		}
	}

	private void sendSkipMessage(Process process, Link link) {
		new Thread(() -> sendMessage(skipMessage(process, process.state(link.to())))).start();
	}

	private boolean anyPredecessorHasExited(Process process, Link link) {
		return process.predecessorsFinishedStatus(link.to()).stream().anyMatch(s -> s.stateInfo().status().equals("Exit"));
	}

	private ProcessStatus enterMessage(Process process, State state) {
		return stateMessage(process, state, "Enter");
	}

	private ProcessStatus exitMessage(Process process, State state) {
		return stateMessage(process, state, "Exit");
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
