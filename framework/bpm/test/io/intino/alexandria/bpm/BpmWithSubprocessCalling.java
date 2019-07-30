package io.intino.alexandria.bpm;

import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageHub;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.intino.alexandria.bpm.Link.Type.Inclusive;
import static io.intino.alexandria.bpm.State.Type.Initial;
import static io.intino.alexandria.bpm.State.Type.Terminal;
import static io.intino.alexandria.bpm.Task.Type.Automatic;
import static io.intino.alexandria.bpm.Task.Type.CallActivity;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BpmWithSubprocessCalling extends BpmTest {

	private static final String ProcessStatus = "ProcessStatus";
	private static Map<String, String> memory = new HashMap<>();
	private static MessageHub messageHub;

	@Test
	public void name() throws InterruptedException {
		messageHub = new MessageHub_();
		PersistenceManager.InMemoryPersistenceManager persistence = new PersistenceManager.InMemoryPersistenceManager();
		new Workflow(messageHub, new ProcessFactory(), persistence);
		messageHub.sendMessage(ProcessStatus, createProcessMessage());
		waitForProcess(persistence);
		List<ProcessStatus> messages = messagesOf(persistence.read("finished/1.process"));
		assertThat(messages.get(1).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(1).stateInfo().status(), is("Enter"));
		assertThat(messages.get(2).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(2).stateInfo().status(), is("Exit"));
		if (exitStateStatus(messages, "CreateString").taskInfo().result().equals("Hello"))
			assertThat(exitStateStatus(messages, "HandleSubprocessEnding").taskInfo().result(), is("true"));
		else assertThat(exitStateStatus(messages, "HandleSubprocessEnding").taskInfo().result(), is("false"));
	}

	private Message createProcessMessage() {
		return new Message(ProcessStatus)
				.set("ts", "2019-01-01T00:00:00Z")
				.set("id", "1")
				.set("name", "StringContentReviewer")
				.set("status", "Enter");
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
			return new Task(Automatic) {
				@Override
				public String execute() {
					memory.put(id(), Math.random() < 0.5 ? "Hello" : "Goodbye");
					return memory.get(id());
				}

			};
		}

		private Task callSubprocess() {
			return new Task(CallActivity) {
				@Override
				public String execute() {
					messageHub.sendMessage(ProcessStatus, new ProcessStatus("2", "StringChecker", "Enter", "1", "1", "CallSubprocess").message());
					return "subprocess called StringChecker";
				}
			};
		}

		private Task handleSubprocessEnding() {
			return new Task(Automatic) {
				@Override
				public String execute() {
					return memory.get("2");
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
			return new Task(Automatic) {
				@Override
				public String execute() {
					return memory.put(id(), memory.get(owner()).equals("Hello") + "");
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
