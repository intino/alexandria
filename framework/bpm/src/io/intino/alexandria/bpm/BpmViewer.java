package io.intino.alexandria.bpm;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageReader;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.StreamSupport.stream;

public class BpmViewer {

	private final PersistenceManager persistenceManager;

	public BpmViewer(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}

	public ProcessInfo processInfo(String processId) {
		String path = "active/" + processId + ".process";
		if (persistenceManager.exists(path)) return new ProcessInfo(path);
		Timetag timetag = Timetag.of(Instant.now(), Scale.Month);
		Timetag until = timetag.previous(10);
		while (!timetag.isBefore(until)) {
			path = "finished/" + timetag + "/" + processId + ".process";
			if (persistenceManager.exists(path)) return new ProcessInfo(path);
			timetag = timetag.previous();
		}
		return null;
	}

	public String activePathOf(String processId) {
		return "active/" + processId + ".process";
	}

	public String finishedPathOf(String timetag, String processId) {
		return "finished/" + timetag + "/" + processId + ".process";
	}

	public List<ProcessInfo> activeProcesses() {
		return persistenceManager.list("active").stream()
				.map(ProcessInfo::new)
				.collect(Collectors.toList());
	}

	public List<ProcessInfo> finishedProcesses(Timetag timetag) {
		return persistenceManager.list("finished/" + timetag).stream()
				.map(ProcessInfo::new)
				.collect(Collectors.toList());
	}

	public class ProcessInfo {
		private final String processPath;
		private final String dataPath;

		public ProcessInfo(String processPath) {
			this.processPath = processPath;
			this.dataPath = processPath.replace(".process", ".data");
		}

		public List<ProcessStatus> processStatuses() {
			return stream(new MessageReader(persistenceManager.read(processPath)).spliterator(), false)
					.map(ProcessStatus::new).collect(toList());
		}

		public Map<String, String> data() {
			Message message = new MessageReader(persistenceManager.read(dataPath)).next();
			return message.attributes().stream().collect(toMap(a -> a, a -> message.get(a).asString()));
		}

		public boolean isFinished(){
			return processPath.contains("finished");
		}

		public String processPath() {
			return processPath;
		}
	}


}
