package io.intino.alexandria.bpm;

import io.intino.alexandria.inl.Message;
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

public class BpmWithSubprocessCalling {

	public static final String ProcessStatus = "ProcessStatus";
	private static Map<String, String> memory = new HashMap<>();
	private static MessageHub messageHub;

	@Test
	public void name() throws InterruptedException {
		messageHub = new MessageHub_();
		Workflow workflow = new Workflow(messageHub, new ProcessFactory());
		messageHub.sendMessage(ProcessStatus, createProcessMessage());
		Process process1 = workflow.process("1");
		Process process2 = workflow.process("2");
		while(!hasEnded(process1, process2)){
			if(process1==null) process1 = workflow.process("1");
			if(process2==null) process2 = workflow.process("2");
			Thread.sleep(100);
		}
		List<ProcessStatus> messages = process1.messages(); //TODO
		assertThat(messages.get(0).message().toString(), is(createProcessMessage().toString()));
	}

	private boolean hasEnded(Process... processes) {
		for (Process process : processes) {
			if(process == null || process.processStatusList.isEmpty()) return false;
			ProcessStatus message = process.messages().get(process.processStatusList.size() - 1);
			if (!message.processStatus().equals("Exit")) return false;
		}
		return true;
	}

	private Message createProcessMessage() {
		return new Message(ProcessStatus)
				.set("ts", "2019-01-01T00:00:00Z")
				.set("id", "1")
				.set("name", "StringContentReviewer")
				.set("status", "Enter");
	}

	static class StringContentReviewerProcess extends Process {

		protected StringContentReviewerProcess(String id) {
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
					return "create string executed";
				}

			};
		}

		private Task callSubprocess() {
			return new Task(CallActivity) {
				@Override
				String execute() {
					messageHub.sendMessage(ProcessStatus, new Message(ProcessStatus)
							.set("ts", "2019-01-01T00:00:00Z")
							.set("id", "2")
							.set("name", "StringChecker")
							.set("owner", "1")
							.set("callbackProcess", "1")
							.set("callbackState", "CallSubprocess")
							.set("status", "Enter"));
					return "subprocess called StringChecker";
				}
			};
		}

		private Task handleSubprocessEnding() {
			return new Task(Automatic) {
				@Override
				String execute() {
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

		protected StringCheckerProcess(String id) {
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
