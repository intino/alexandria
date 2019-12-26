package io.intino.alexandria.bpm;

import io.intino.alexandria.bpm.PersistenceManager.InMemoryPersistenceManager;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static io.intino.alexandria.bpm.Link.Type.Exclusive;
import static io.intino.alexandria.bpm.State.Type.Initial;
import static io.intino.alexandria.bpm.State.Type.Terminal;
import static io.intino.alexandria.bpm.Task.Type.Automatic;
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
		}.send(createProcessMessage());
		waitForProcess(manager);
		List<ProcessStatus> messages = messagesOf(manager.read("finished/1.process"));
		assertThat(messages.get(0).processStatus(), is("Enter"));
		assertThat(messages.get(1).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(1).stateInfo().status(), is("Enter"));
		assertThat(messages.get(2).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(2).stateInfo().status(), is("Exit"));
		assertThat(messages.get(3).stateInfo().name(), is("CheckContainsHello"));
		assertThat(messages.get(3).stateInfo().status(), is("Enter"));
		assertThat(messages.get(4).stateInfo().name(), is("CheckContainsHello"));
		assertThat(messages.get(4).stateInfo().status(), is("Exit"));
		Map<String, String> data = data(manager, "finished/1.data");
		if (data.get("CreateString").equals("Hello")) {
			assertThat(data.get("ProcessHello"), is("Processing hello"));
			assertThat(exitStateStatus(messages, "ProcessGoodbye").stateInfo().status(), is("Rejected"));
		} else {
			assertThat(data.get("ProcessGoodbye"), is("Processing goodbye"));
			assertThat(exitStateStatus(messages, "ProcessHello").stateInfo().status(), is("Rejected"));
		}
	}

	private ProcessStatus createProcessMessage() {
		return new ProcessStatus("1", "StringContentReviewer", Process.Status.Enter);
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
			return new Task(Automatic) {
				@Override
				public void execute() {
					data.put("CreateString", Math.random() < 0.5 ? "Hello" : "Goodbye");
				}

			};
		}

		private Task checkContainsHelloTask() {
			return new Task(Automatic) {
				@Override
				public void execute() {
					data.put("CheckContainsHello", data.get("CreateString").contains("Hello") + "");
				}
			};
		}

		private Task processHello() {
			return new Task(Automatic) {

				@Override
				public boolean accept() {
					return data.get("CheckContainsHello").equals("true");
				}

				@Override
				public void execute() {
					data.put("ProcessHello", "Processing hello");
				}
			};
		}

		private Task processGoodbye() {
			return new Task(Automatic) {

				@Override
				public void execute() {
					data.put("ProcessGoodbye", "Processing goodbye");
				}
			};
		}

		@Override
		public String name() {
			return "StringContentReviewer";
		}
	}

}
