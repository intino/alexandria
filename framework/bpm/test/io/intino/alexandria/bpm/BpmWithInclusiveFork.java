package io.intino.alexandria.bpm;

import io.intino.alexandria.message.Message;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static io.intino.alexandria.bpm.Link.Type.Inclusive;
import static io.intino.alexandria.bpm.State.Type.Initial;
import static io.intino.alexandria.bpm.State.Type.Terminal;
import static io.intino.alexandria.bpm.Task.Type.Automatic;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BpmWithInclusiveFork extends BpmTest {

	@Test
	public void name() throws InterruptedException {
		PersistenceManager.InMemoryPersistenceManager persistence = new PersistenceManager.InMemoryPersistenceManager();
		new Workflow((id, name) -> new JoinTwoBranches(id), persistence) {

			@Override
			public void send(ProcessStatus processStatus) {
				new Thread(() -> receive(processStatus)).start();
			}
		}.send(createProcessMessage());
		waitForProcess(persistence);
		List<ProcessStatus> messages = messagesOf(persistence.read("finished/1.process"));
		assertThat(messages.get(1).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(1).stateInfo().status(), is("Enter"));
		assertThat(messages.get(2).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(2).stateInfo().status(), is("Exit"));
		Map<String, String> data = data(persistence, "finished/1.data");
		if (data.get("CreateString").equals("Hello:Goodbye"))
			assertThat(data.get("JoinResult"), is("Hi:Bye"));
		else assertThat(data.get("JoinResult"), is("Bye:Hi"));
	}

	private ProcessStatus createProcessMessage() {
		return new ProcessStatus(new Message("ProcessStatus")
				.set("ts", "2019-01-01T00:00:00Z")
				.set("id", "1")
				.set("name", "StringContentReviewer")
				.set("status", "Enter"));
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
				public void execute() {
					data.put("CreateString", Math.random() < 0.5 ? "Hello:Goodbye" : "Goodbye:Hello");
				}

			};
		}

		private Task processHello() {
			return new Task(Automatic) {

				@Override
				public void execute() {
					String[] result = data.get("CreateString").split(":");
					data.put("ProcessHello", result[0].equals("Hello") ? "0-Hi" : "1-Hi");
				}
			};
		}

		private Task processGoodbye() {
			return new Task(Automatic) {

				@Override
				public void execute() {
					String[] result = data.get("CreateString").split(":");
					data.put("ProcessGoodbye", result[0].equals("Goodbye") ? "0-Bye" : "1-Bye");
				}
			};
		}


		private Task joinResult() {
			return new Task(Automatic) {

				@Override
				public void execute() {
					String[] hello = data.get("ProcessHello").split("-");
					String[] goodbye = data.get("ProcessGoodbye").split("-");
					data.put("JoinResult", hello[0].equals("0") ? hello[1] + ":" + goodbye[1] : goodbye[1] + ":" + hello[1]);
				}
			};
		}

		@Override
		public String name() {
			return "StringContentReviewer";
		}
	}

}
