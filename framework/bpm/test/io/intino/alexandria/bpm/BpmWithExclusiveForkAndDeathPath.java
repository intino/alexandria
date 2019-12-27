package io.intino.alexandria.bpm;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static io.intino.alexandria.bpm.Link.Type.Exclusive;
import static io.intino.alexandria.bpm.Link.Type.Inclusive;
import static io.intino.alexandria.bpm.State.Type.Initial;
import static io.intino.alexandria.bpm.State.Type.Terminal;
import static io.intino.alexandria.bpm.Task.Type.Automatic;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BpmWithExclusiveForkAndDeathPath extends BpmTest {

	@Test
	public void name() throws InterruptedException {
		PersistenceManager.InMemoryPersistenceManager persistence = new PersistenceManager.InMemoryPersistenceManager();
		new Workflow((id, name) -> new StringContentReviewerProcess(id), persistence) {

			@Override
			public void send(ProcessStatus processStatus) {
				new Thread(() -> receive(processStatus)).start();
			}
		}.registerProcess(new StringContentReviewerProcess("1"));
		waitForProcess(persistence);
		List<ProcessStatus> messages = messagesOf(persistence.read("finished/1.process"));
		assertThat(messages.get(0).processStatus(), is("Enter"));
		assertThat(messages.get(1).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(1).stateInfo().status(), is("Enter"));
		assertThat(messages.get(2).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(2).stateInfo().status(), is("Exit"));
		assertThat(messages.get(3).stateInfo().name(), is("CheckContainsHello"));
		assertThat(messages.get(3).stateInfo().status(), is("Enter"));
		assertThat(messages.get(4).stateInfo().name(), is("CheckContainsHello"));
		assertThat(messages.get(4).stateInfo().status(), is("Exit"));

		Map<String, String> data = data(persistence, "finished/1.data");
		if (data.get("CreateString").equals("Hello")) {
			assertThat(data.get("ProcessHello2"), is("Processing hello2"));
			assertThat(exitStateStatus(messages, "ProcessGoodbye2").stateInfo().status(), is("Skipped"));
			assertThat(data.get("Terminate"), is("hello2"));
		} else {
			assertThat(data.get("ProcessGoodbye2"), is("Processing goodbye2"));
			assertThat(exitStateStatus(messages, "ProcessHello2").stateInfo().status(), is("Skipped"));
			assertThat(data.get("Terminate"), is("bye2"));
		}
	}

	static class StringContentReviewerProcess extends Process {

		StringContentReviewerProcess(String id) {
			super(id);
			addState(new State("CreateString", createString(), Initial));
			addState(new State("CheckContainsHello", checkContainsHelloTask()));
			addState(new State("ProcessHello", processHello()));
			addState(new State("ProcessHello2", processHello2()));
			addState(new State("ProcessGoodbye", processGoodbye()));
			addState(new State("ProcessGoodbye2", processGoodbye2()));
			addState(new State("Terminate", terminate(), Terminal));
			addLink(new Link("CreateString", "CheckContainsHello", Exclusive));
			addLink(new Link("CheckContainsHello", "ProcessHello", Exclusive));
			addLink(new Link("CheckContainsHello", "ProcessGoodbye", Exclusive));
			addLink(new Link("ProcessHello", "ProcessHello2", Inclusive));
			addLink(new Link("ProcessGoodbye", "ProcessGoodbye2", Inclusive));
			addLink(new Link("ProcessHello2", "Terminate", Inclusive));
			addLink(new Link("ProcessGoodbye2", "Terminate", Inclusive));
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
					data.put("CheckContainsHello", data.get("CreateString").equals("Hello") + "");
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
				}
			};
		}

		private Task processHello2() {
			return new Task(Automatic) {

				@Override
				public void execute() {
					data.put("ProcessHello2", "Processing hello2");
				}
			};
		}

		private Task processGoodbye() {
			return new Task(Automatic) {

				@Override
				public void execute() {
				}
			};
		}

		private Task terminate() {
			return new Task(Automatic) {

				@Override
				public void execute() {
					data.put("Terminate", exitStateStatus("ProcessHello2").stateInfo().status().equals("Exit") ? "hello2" :
							exitStateStatus("ProcessGoodbye2").stateInfo().status().equals("Exit") ? "bye2" : "none");
				}
			};
		}

		private Task processGoodbye2() {
			return new Task(Automatic) {

				@Override
				public void execute() {
					data.put("ProcessGoodbye2", "Processing goodbye2");
				}
			};
		}

		@Override
		public String name() {
			return "StringContentReviewer";
		}
	}

}
