package io.intino.alexandria.bpm;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.bpm.PersistenceManager.InMemoryPersistenceManager;
import org.junit.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static io.intino.alexandria.Scale.Month;
import static io.intino.alexandria.bpm.Link.Type.Exclusive;
import static io.intino.alexandria.bpm.State.Type.Initial;
import static io.intino.alexandria.bpm.State.Type.Terminal;
import static io.intino.alexandria.bpm.Task.Type.Default;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BpmWithExclusiveFork extends BpmTest {

	@Test
	public void name() throws InterruptedException {
		InMemoryPersistenceManager manager = new InMemoryPersistenceManager();
		new Workflow((id, name) -> new StringContentReviewerProcess(id), manager) {

			@Override
			public void send(ProcessStatus processStatus) {
				new Thread(() -> receive(processStatus)).start();
			}
		}.registerProcess(new StringContentReviewerProcess("1"));
		waitForProcess(manager);
		List<ProcessStatus> messages = messagesOf(manager.read("finished/" + Timetag.of(Instant.now(), Month) + "/1.process"));
		assertThat(messages.get(0).processStatus(), is("Enter"));
		assertThat(messages.get(1).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(1).stateInfo().status(), is("Enter"));
		assertThat(messages.get(2).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(2).stateInfo().status(), is("Exit"));
		assertThat(messages.get(3).stateInfo().name(), is("CheckContainsHello"));
		assertThat(messages.get(3).stateInfo().status(), is("Enter"));
		assertThat(messages.get(4).stateInfo().name(), is("CheckContainsHello"));
		assertThat(messages.get(4).stateInfo().status(), is("Exit"));
		Map<String, String> data = data(manager, "finished/" + Timetag.of(Instant.now(), Month) + "/1.data");
		if (data.get("createstring").equals("Hello")) {
			assertThat(data.get("processhello"), is("Processing hello"));
			assertThat(exitStateStatus(messages, "ProcessGoodbye").stateInfo().status(), is("Rejected"));
		} else {
			assertThat(data.get("processgoodbye"), is("Processing goodbye"));
			assertThat(exitStateStatus(messages, "ProcessHello").stateInfo().status(), is("Rejected"));
		}
	}

	static class StringContentReviewerProcess extends Process {

		StringContentReviewerProcess(String id) {
			super(id);
			addState(new State("CreateString", createString(), Initial));
			addState(new State("CheckContainsHello", checkContainsHelloTask()));
			addState(new State("ProcessHello", processHello(), Terminal));
			addState(new State("ProcessGoodbye", processGoodbye(), Terminal));
			addLink(new Link("CreateString", "CheckContainsHello", Exclusive));
			addLink(new Link("CheckContainsHello", "ProcessHello", Exclusive));
			addLink(new Link("CheckContainsHello", "ProcessGoodbye", Exclusive));
		}

		private Task createString() {
			return new Task(Default) {
				@Override
				public void execute() {
					put("CreateString", Math.random() < 0.5 ? "Hello" : "Goodbye");
				}

			};
		}

		private Task checkContainsHelloTask() {
			return new Task(Default) {
				@Override
				public void execute() {
					put("CheckContainsHello", get("CreateString").contains("Hello") + "");
				}
			};
		}

		private Task processHello() {
			return new Task(Default) {

				@Override
				public boolean accept() {
					return get("CheckContainsHello").equals("true");
				}

				@Override
				public void execute() {
					put("ProcessHello", "Processing hello");
				}
			};
		}

		private Task processGoodbye() {
			return new Task(Default) {

				@Override
				public void execute() {
					put("ProcessGoodbye", "Processing goodbye");
				}
			};
		}

		@Override
		public String name() {
			return "StringContentReviewer";
		}
	}

}
