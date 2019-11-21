package io.intino.alexandria.bpm;

import io.intino.alexandria.message.Message;
import org.junit.Test;

import java.util.List;

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
		new Workflow((id, name) -> new StringContentReviewerProcess(id), persistence, null) {

			@Override
			public void send(ProcessStatus processStatus) {
				new Thread(() -> receive(processStatus)).start();
			}
		}.send(createProcessMessage());
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
		if (exitStateStatus(messages, "CreateString").taskInfo().result().equals("Hello")) {
			assertThat(exitStateStatus(messages, "ProcessHello2").taskInfo().result(), is("Processing hello2"));
			assertThat(exitStateStatus(messages, "ProcessGoodbye2").stateInfo().status(), is("Skipped"));
			assertThat(exitStateStatus(messages, "Terminate").taskInfo().result(), is("hello2"));
		} else {
			assertThat(exitStateStatus(messages, "ProcessGoodbye2").taskInfo().result(), is("Processing goodbye2"));
			assertThat(exitStateStatus(messages, "ProcessHello2").stateInfo().status(), is("Skipped"));
			assertThat(exitStateStatus(messages, "Terminate").taskInfo().result(), is("bye2"));
		}
	}


	private ProcessStatus createProcessMessage() {
		return new ProcessStatus(new Message("ProcessStatus")
				.set("ts", "2019-01-01T00:00:00Z")
				.set("id", "1")
				.set("name", "StringContentReviewer")
				.set("status", "Enter"));
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
				public Result execute() {
					return new Result(Math.random() < 0.5 ? "Hello" : "Goodbye");
				}

			};
		}

		private Task checkContainsHelloTask() {
			return new Task(Automatic) {
				@Override
				public Result execute() {
					ProcessStatus last = exitStateStatus("CreateString");
					return new Result(last.taskInfo().result().contains("Hello") + "");
				}
			};
		}

		private Task processHello() {
			return new Task(Automatic) {

				@Override
				public boolean accept() {
					ProcessStatus last = exitStateStatus("CheckContainsHello");
					return last.taskInfo().result().equals("true");
				}

				@Override
				public Result execute() {
					return new Result("Processing hello");
				}
			};
		}

		private Task processHello2() {
			return new Task(Automatic) {

				@Override
				public Result execute() {
					return new Result("Processing hello2");
				}
			};
		}

		private Task processGoodbye() {
			return new Task(Automatic) {

				@Override
				public Result execute() {
					return new Result("Processing goodbye");
				}
			};
		}

		private Task terminate() {
			return new Task(Automatic) {

				@Override
				public Result execute() {
					return new Result(exitStateStatus("ProcessHello2").stateInfo().status().equals("Exit") ? "hello2" :
							exitStateStatus("ProcessGoodbye2").stateInfo().status().equals("Exit") ? "bye2" : "none");
				}
			};
		}

		private Task processGoodbye2() {
			return new Task(Automatic) {

				@Override
				public Result execute() {
					return new Result("Processing goodbye2");
				}
			};
		}

		@Override
		public String name() {
			return "StringContentReviewer";
		}
	}

}
