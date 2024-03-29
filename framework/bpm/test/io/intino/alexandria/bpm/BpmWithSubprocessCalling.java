package io.intino.alexandria.bpm;

import io.intino.alexandria.message.Message;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.intino.alexandria.bpm.Link.Type.Inclusive;
import static io.intino.alexandria.bpm.State.Type.Initial;
import static io.intino.alexandria.bpm.State.Type.Terminal;
import static io.intino.alexandria.bpm.Task.Type.CallActivity;
import static io.intino.alexandria.bpm.Task.Type.Default;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BpmWithSubprocessCalling extends BpmTest {

	private static final String ProcessStatus = "ProcessStatus";
	private static Map<String, String> memory = new HashMap<>();
	private static Workflow workflow;

	@Test
	public void name() throws InterruptedException {
		PersistenceManager.InMemoryPersistenceManager persistence = new PersistenceManager.InMemoryPersistenceManager();
		workflow = new Workflow(new ProcessFactory(), persistence) {

			@Override
			public void send(ProcessStatus processStatus) {
				new Thread(() -> receive(processStatus)).start();
			}
		};
		workflow.receive(createProcessMessage());
		waitForProcess(persistence);
		List<ProcessStatus> messages = messagesOf(persistence.read("finished/201901/1.process"));
		assertThat(messages.get(1).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(1).stateInfo().status(), is("Enter"));
		assertThat(messages.get(2).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(2).stateInfo().status(), is("Exit"));
		Map<String, String> data = data(persistence, "finished/201901/1.data");
		if (data.get("createstring").equals("Hello"))
			assertThat(data.get("handlesubprocessending"), is("true"));
		else assertThat(data.get("handlesubprocessending"), is("false"));
	}

	private ProcessStatus createProcessMessage() {
		return new ProcessStatus(new Message(ProcessStatus)
				.set("ts", "2019-01-01T00:00:00Z")
				.set("id", "1")
				.set("name", "StringContentReviewer")
				.set("status", "Enter"));
	}

	static class StringContentReviewerProcess extends Process {

		StringContentReviewerProcess(String id) {
			super(id);
			addState(new State("CreateString", createString(), Initial));
			addState(new State("CallSubprocess", callSubprocess()));
			addState(new State("HandleSubprocessEnding", handleSubprocessEnding(), Terminal));
			addLink(new Link("CreateString", "CallSubprocess", Inclusive));
			addLink(new Link("CallSubprocess", "HandleSubprocessEnding", Inclusive));
		}

		private Task createString() {
			return new Task(Default) {
				@Override
				public void execute() {
					memory.put(id(), Math.random() < 0.5 ? "Hello" : "Goodbye");
					put("CreateString", memory.get(id()));
				}

			};
		}

		private Task callSubprocess() {
			return new Task(CallActivity) {
				@Override
				public void execute() {
					workflow.receive(new ProcessStatus("2", "StringChecker", Status.Enter, "1", "1", "CallSubprocess"));
				}
			};
		}

		private Task handleSubprocessEnding() {
			return new Task(Default) {
				@Override
				public void execute() {
					put("HandleSubprocessEnding", memory.get("2"));
				}
			};
		}

		@Override
		public String name() {
			return "StringContentReviewer";
		}
	}

	static class StringCheckerProcess extends Process {

		StringCheckerProcess(String id) {
			super(id);
			addState(new State("CheckString", checkString(), Initial, Terminal));
		}

		private Task checkString() {
			return new Task(Default) {
				@Override
				public void execute() {
					memory.put(id(), memory.get(owner()).equals("Hello") + "");
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
