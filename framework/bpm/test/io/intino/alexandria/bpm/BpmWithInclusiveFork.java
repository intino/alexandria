package io.intino.alexandria.bpm;

import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageHub;
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
		MessageHub messageHub = new MessageHub_();
		Workflow workflow = new Workflow(messageHub, new ProcessFactory());
		messageHub.sendMessage("ProcessStatus", createProcessMessage());
		Process process = workflow.process("1");
		while(!hasEnded(process)){
			if(process==null) process = workflow.process("1");
			Thread.sleep(100);
		}
		List<ProcessStatus> messages = process.messages();
		assertThat(messages.get(1).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(1).stateInfo().status(), is("Enter"));
		assertThat(messages.get(2).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(2).stateInfo().status(), is("Exit"));
		if(process.exitStateStatus("CreateString").taskInfo().result().equals("Hello:Goodbye"))
			assertThat(process.exitStateStatus("JoinResult").taskInfo().result(), is("Hi:Bye"));
		else assertThat(process.exitStateStatus("JoinResult").taskInfo().result(), is("Bye:Hi"));
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
					return Math.random() < 0.5 ? "Hello:Goodbye" : "Goodbye:Hello";
				}

			};
		}

		private Task processHello() {
			return new Task(Automatic) {

				@Override
				String execute() {
					String[] result = resultOfState("CreateString").split(":");
					return result[0].equals("Hello") ? "0-Hi" : "1-Hi";
				}
			};
		}

		private String resultOfState(String stateName) {
			return new ArrayList<>(processStatusList).stream().filter(m -> {
				if(!m.hasStateInfo()) return false;
				return m.stateInfo().name().equals(stateName) && m.stateInfo().status().equals("Exit");
			}).findFirst().get().taskInfo().result();
		}

		private Task processGoodbye() {
			return new Task(Automatic) {

				@Override
				String execute() {
					String[] result = resultOfState("CreateString").split(":");
					return result[0].equals("Goodbye") ? "0-Bye" : "1-Bye";
				}
			};
		}


		private Task joinResult() {
			return new Task(Automatic) {

				@Override
				String execute() {
					String[] hello = resultOfState("ProcessHello").split("-");
					String[] goodbye = resultOfState("ProcessGoodbye").split("-");
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
