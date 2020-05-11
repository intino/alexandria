package io.intino.alexandria.bpm;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageReader;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
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
				.map(processPath -> new ProcessInfo("active/" + processPath))
				.collect(Collectors.toList());
	}

	public List<ProcessInfo> finishedProcesses(Timetag timetag) {
		return persistenceManager.list("finished/" + timetag).stream()
				.map(processPath -> new ProcessInfo("finished/" + timetag + "/" + processPath))
				.collect(Collectors.toList());
	}

	public class ProcessInfo {
		private final String processPath;
		private final String dataPath;
		private final String definitionPath;
		private List<ProcessStatus> processStatusList;
		private Definition definition;
		private Map<String, String> data;

		public ProcessInfo(String processPath) {
			this.processPath = processPath;
			this.dataPath = processPath.replace(".process", ".data");
			this.definitionPath = processPath.replace(".process", ".definition");
		}

		public List<ProcessStatus> processStatuses() {
			if(processStatusList != null) return processStatusList;
			MessageReader messageReader = new MessageReader(persistenceManager.read(processPath));
			processStatusList = stream(messageReader.spliterator(), false).map(ProcessStatus::new).collect(toList());
			messageReader.close();
			return processStatusList;
		}

		public Definition definition(){
			if(definition != null) return definition;
			if(!persistenceManager.exists(definitionPath)) return null;
			MessageReader reader = new MessageReader(persistenceManager.read(definitionPath));
			List<Message> messages = stream(reader.spliterator(), false).collect(toList());
			List<Definition.State> states = messages.stream().filter(m -> m.type().equals("State"))
					.map(m -> new Definition.State() {
						@Override
						public String name() {
							return m.get("name").asString();
						}

						@Override
						public List<String> types() {
							return asList(m.get("type").asString().split(", "));
						}

						@Override
						public String taskType() {
							return m.get("taskType").asString();
						}
					}).collect(toList());
			List<Definition.Link> links = messages.stream().filter(m -> m.type().equals("Link"))
					.map(m -> new Definition.Link() {
						@Override
						public String from() {
							return m.get("from").asString();
						}

						@Override
						public String to() {
							return m.get("to").asString();
						}

						@Override
						public String type() {
							return m.get("type").asString();
						}

					}).collect(toList());
			definition = new Definition() {
				@Override
				public List<State> states() {
					return states;
				}

				@Override
				public List<Link> links() {
					return links;
				}
			};
			reader.close();
			return definition;
		};

		public Map<String, String> data() {
			if(data != null) return data;
			MessageReader reader = new MessageReader(persistenceManager.read(dataPath));
			Message message = reader.next();
			data = message.attributes().stream().collect(toMap(a -> a, a -> message.get(a).asString()));
			reader.close();
			return data;
		}

		public boolean isFinished() {
			return processPath.contains("finished");
		}

		public String processPath() {
			return processPath;
		}

		public String id() {
			return processStatuses().get(0).processId();
		}

		public String name() {
			return processStatuses().get(0).processName();
		}
	}

	public interface Definition{
		List<State> states();
		List<Link> links();

		interface State{
			String name();
			List<String> types();
			String taskType();
		}

		interface Link{
			String from();
			String to();
			String type();
		}
	}

}
