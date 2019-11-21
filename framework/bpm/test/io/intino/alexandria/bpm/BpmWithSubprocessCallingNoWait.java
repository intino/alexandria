package io.intino.alexandria.bpm;

import io.intino.alexandria.message.Message;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.intino.alexandria.bpm.Link.Type.Inclusive;
import static io.intino.alexandria.bpm.State.Type.Initial;
import static io.intino.alexandria.bpm.State.Type.Terminal;
import static io.intino.alexandria.bpm.Task.Type.Automatic;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BpmWithSubprocessCallingNoWait extends BpmTest {

	public static final String ProcessStatus = "ProcessStatus";
	private static Map<String, String> memory = new HashMap<>();
	private static Workflow workflow;

	@Test
	public void name() throws InterruptedException {
		PersistenceManager.InMemoryPersistenceManager persistence = new PersistenceManager.InMemoryPersistenceManager();
		workflow = new Workflow(new ProcessFactory(), persistence, null) {

			@Override
			public void send(ProcessStatus processStatus) {
				new Thread(() -> receive(processStatus)).start();
			}
		};
		workflow.send(createProcessMessage());
		waitForProcess(persistence);
		List<ProcessStatus> messages1 = messagesOf(persistence.read("finished/1.process"));
		List<ProcessStatus> messages2 = messagesOf(persistence.read("finished/2.process"));
		assertThat(messages1.get(0).message().toString(), is(createProcessMessage().message().toString()));
	}

	private ProcessStatus createProcessMessage() {
		return new ProcessStatus(new Message(ProcessStatus)
				.set("ts", "2019-01-01T00:00:00Z")
				.set("id", "1")
				.set("name", "StringContentReviewer")
				.set("status", "Enter"));
	}

	static class StringContentReviewerProcess extends Process {

		protected StringContentReviewerProcess(String id) {
			super(id);
			addState(new State("CreateString", createString(), Initial));
			addState(new State("CallSubprocess", callSubprocess(), Terminal));
			addLink(new Link("CreateString", "CallSubprocess", Inclusive));
		}

		private Task createString() {
			return new Task(Automatic) {
				@Override
				public Result execute() {
					memory.put(id(), Math.random() < 0.5 ? "Hello" : "Goodbye");
					return new Result("create string executed");
				}

			};
		}

		private Task callSubprocess() {
			return new Task(Automatic) {
				@Override
				public Result execute() {
					workflow.send(new ProcessStatus(new Message(ProcessStatus)
							.set("ts", "2019-01-01T00:00:00Z")
							.set("id", "2")
							.set("name", "StringChecker")
							.set("owner", "1")
							.set("status", "Enter")));
					return new Result("subprocess called StringChecker");
				}
			};
		}

		@Override
		public String name() {
			return "StringContentReviewer";
		}
	}

	static class StringCheckerProcess extends Process {

		protected StringCheckerProcess(String id) {
			super(id);
			addState(new State("CheckString", checkString(), Initial, Terminal));
		}

		private Task checkString() {
			return new Task(Automatic) {
				@Override
				public Result execute() {
					return new Result(memory.get(owner()).equals("Hello") + "");
				}

			};
		}

		@Override
		public String name() {
			return "StringChecker";
		}
	}

	public static class ProcessFactory implements io.intino.alexandria.bpm.ProcessFactory {

		@Override
		public Process createProcess(String id, String name) {
			return name.equals("StringChecker") ? new StringCheckerProcess(id) : new StringContentReviewerProcess(id);
		}
	}
}
