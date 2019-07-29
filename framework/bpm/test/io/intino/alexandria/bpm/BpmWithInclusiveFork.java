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

public class BpmWithInclusiveFork extends BpmTest {

	@Test
	public void name() throws InterruptedException {
		MessageHub messageHub = new MessageHub_();
		PersistenceManager.InMemoryPersistenceManager persistence = new PersistenceManager.InMemoryPersistenceManager();
		new Workflow(messageHub, (id, name) -> new JoinTwoBranches(id), persistence);
		messageHub.sendMessage("ProcessStatus", createProcessMessage());
		waitForProcess(persistence);
		List<ProcessStatus> messages = messagesOf(persistence.read("finished/1.process"));
		assertThat(messages.get(1).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(1).stateInfo().status(), is("Enter"));
		assertThat(messages.get(2).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(2).stateInfo().status(), is("Exit"));
		if (exitStateStatus(messages, "CreateString").taskInfo().result().equals("Hello:Goodbye"))
			assertThat(exitStateStatus(messages, "JoinResult").taskInfo().result(), is("Hi:Bye"));
		else assertThat(exitStateStatus(messages, "JoinResult").taskInfo().result(), is("Bye:Hi"));
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
				if (!m.hasStateInfo()) return false;
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

}
