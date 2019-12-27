package io.intino.alexandria.bpm;

import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.StreamSupport.stream;

abstract class BpmTest {
	List<ProcessStatus> messagesOf(InputStream read) {
		return stream(new MessageReader(read).spliterator(), false).map(ProcessStatus::new).collect(toList());
	}

	void waitForProcess(PersistenceManager.InMemoryPersistenceManager persistence) throws InterruptedException {
		Thread.sleep(100);
		while (!persistence.list("active/").isEmpty() ||
				persistence.list("finished/").isEmpty()) Thread.sleep(100);
	}

	Map<String, String> data(PersistenceManager.InMemoryPersistenceManager persistence, String path) {
		Message message = new MessageReader(persistence.read(path)).next();
		return message.attributes().stream().collect(toMap(a -> a, a -> message.get(a).asString()));
	}

	ProcessStatus exitStateStatus(List<ProcessStatus> processStatusList, String stateName) {
		return new ArrayList<>(processStatusList).stream()
				.filter(s -> s.hasStateInfo() && s.stateInfo().name().equals(stateName) && s.stateInfo().isTerminated())
				.findFirst().orElse(null);
	}
}
