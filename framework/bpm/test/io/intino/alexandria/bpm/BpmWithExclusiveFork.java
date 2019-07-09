package io.intino.alexandria.bpm;

import io.intino.alexandria.inl.Message;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.intino.alexandria.bpm.Link.Type.Exclusive;
import static io.intino.alexandria.bpm.State.Type.Initial;
import static io.intino.alexandria.bpm.State.Type.Terminal;
import static io.intino.alexandria.bpm.Task.Type.Automatic;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BpmWithExclusiveFork {

	@Test
	public void name() throws InterruptedException {
		MessageHub messageHub = messageHub();
		Workflow workflow = new Workflow(messageHub, new ProcessFactory());
		messageHub.sendMessage("ProcessStatus", createProcessMessage());
		Process process = workflow.process("1");
		while(!hasEnded(process)){
			if(process==null) process = workflow.process("1");
			Thread.sleep(100);
		}
		List<ProcessStatus> messages = process.messages();
		assertThat(messages.get(0).message().toString(), is(createProcessMessage().toString()));
	}

	private boolean hasEnded(Process process) {
		if(process == null || process.processStatusList.isEmpty()) return false;
		ProcessStatus message = process.messages().get(process.processStatusList.size() - 1);
		return message.processStatus().equals("Exit");
	}

	private Message createProcessMessage() {
		return new Message("ProcessStatus")
				.set("ts", "2019-01-01T00:00:00Z")
				.set("id", "1")
				.set("name", "StringContentReviewer")
				.set("status", "Enter");
	}

	private MessageHub messageHub() {
		return new MessageHub() {
			private List<OnMessageReceived> listeners = new ArrayList<>();

			@Override
			public void sendMessage(String channel, Message message) {
				new Thread(() -> listeners.forEach(l -> l.process(message))).start();
			}

			@Override
			public void registerListener(String channel, OnMessageReceived onMessageReceived) {
				listeners.add(onMessageReceived);
			}
		};
	}

	static class StringContentReviewerProcess extends Process {

		protected StringContentReviewerProcess(String id) {
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
				public String execute() {
					return Math.random() < 0.5 ? "Hello" : "Goodbye";
				}

			};
		}

		private Task checkContainsHelloTask() {
			return new Task(Automatic) {
				@Override
				String execute() {
					ProcessStatus last = exitStateStatus("CreateString");
					return last.taskInfo().result().contains("Hello") + "";
				}
			};
		}

		private ProcessStatus exitStateStatus(String stateName) {
			return new ArrayList<>(processStatusList)
					.stream()
					.filter(s -> s.hasStateInfo() && s.stateInfo().name().equals(stateName) && s.stateInfo().status().equals("Exit"))
					.findFirst().orElse(null); //TODO more exit status
		}

		private Task processHello() {
			return new Task(Automatic) {

				@Override
				public boolean accept() {
					ProcessStatus last = exitStateStatus("CheckContainsHello");
					return last.taskInfo().result().equals("true");
				}

				@Override
				String execute() {
					return "Processing hello";
				}
			};
		}

		private Task processGoodbye() {
			return new Task(Automatic) {

				@Override
				String execute() {
					return "Processing goodbye";
				}
			};
		}

		@Override
		public String name() {
			return "StringContentReviewer";
		}
	}

	public static class ProcessFactory implements io.intino.alexandria.bpm.ProcessFactory {

		@Override
		public Process createProcess(String id, String name) {
			return new StringContentReviewerProcess(id);
		}
	}
}
