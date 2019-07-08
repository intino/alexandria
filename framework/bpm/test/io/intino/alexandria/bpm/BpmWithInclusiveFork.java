package io.intino.alexandria.bpm;

import io.intino.alexandria.inl.Message;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.intino.alexandria.bpm.Link.Type.Inclusive;
import static io.intino.alexandria.bpm.State.Type.Initial;
import static io.intino.alexandria.bpm.State.Type.Terminal;
import static io.intino.alexandria.bpm.Task.Type.Automatic;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BpmWithInclusiveFork {

	@Test
	public void name() throws InterruptedException {
		MessageHub messageHub = messageHub();
		Workflow workflow = new Workflow(messageHub, new ProcessFactory());
		messageHub.sendMessage("ProcessStatus", createProcessMessage());
		Process process = workflow.process("1");
		while(!hasEnded(process)) Thread.sleep(100);
		List<Message> messages = process.messages();
		assertThat(messages.get(0).toString(), is(createProcessMessage().toString()));
	}

	private boolean hasEnded(Process process) {
		if(process == null || process.messages.isEmpty()) return false;
		Message message = process.messages().get(process.messages.size() - 1);
		return message.get("status").data().equals("Exit");
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
				listeners.forEach(l -> l.process(message));
			}

			@Override
			public void registerListener(String channel, OnMessageReceived onMessageReceived) {
				listeners.add(onMessageReceived);
			}
		};
	}

	static class JoinTwoBranches extends Process {

		JoinTwoBranches(String id) {
			super(id);
			addState(new State("CreateString", createString(), Initial));
			addState(new State("ProcessHello", processHello()));
			addState(new State("ProcessGoodbye", processGoodbye()));
			addState(new State("JoinResult", joinResult(), Terminal));
			addLink(new Link("CreateString", "ProcessHello", Inclusive));
			addLink(new Link("CreateString", "ProcessGoodbye", Inclusive));
			addLink(new Link("ProcessHello", "JoinResult", Inclusive));
			addLink(new Link("ProcessGoodbye", "JoinResult", Inclusive));
		}

		private Task createString() {
			return new Task(Automatic) {
				@Override
				public String execute() {
					return "Hello:Goodbye";
				}

			};
		}

		private Task processHello() {
			return new Task(Automatic) {

				@Override
				String execute() {
					String[] result = resultOfState("CreateString").get("result").data().split(":");
					return result[0].equals("Hello") ? "0-Hi" : "1-Hi";
				}
			};
		}

		private Message resultOfState(String stateName) {
			return messages.stream().filter(m -> {
				List<Message> components = m.components("ProcessStatus.State");
				if(components.isEmpty()) return false;
				Message message = components.get(0);
				return message.get("name").data().equals(stateName) && message.get("status").data().equals("Exit");
			}).findFirst().get().components("ProcessStatus.State").get(0).components("ProcessStatus.State.Task").get(0);
		}

		private Task processGoodbye() {
			return new Task(Automatic) {

				@Override
				String execute() {
					String[] result = resultOfState("CreateString").get("result").data().split(":");
					return result[0].equals("Goodbye") ? "0-Bye" : "1-Bye";
				}
			};
		}


		private Task joinResult() {
			return new Task(Automatic) {

				@Override
				String execute() {
					String[] hello = resultOfState("ProcessHello").get("result").data().split("-");
					String[] goodbye = resultOfState("ProcessGoodbye").get("result").data().split("-");
					return hello[0].equals("0") ? hello[1] + ":" + goodbye[1] : goodbye[1] + ":" + hello[1];
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
			return new JoinTwoBranches(id);
		}
	}
}
