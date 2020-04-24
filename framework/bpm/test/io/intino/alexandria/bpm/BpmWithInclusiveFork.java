package io.intino.alexandria.bpm;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.message.Message;
import org.junit.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static io.intino.alexandria.Scale.Month;
import static io.intino.alexandria.bpm.Link.Type.Inclusive;
import static io.intino.alexandria.bpm.State.Type.Initial;
import static io.intino.alexandria.bpm.State.Type.Terminal;
import static io.intino.alexandria.bpm.Task.Type.Default;
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
		List<ProcessStatus> messages = messagesOf(persistence.read("finished/201901/1.process"));
		assertThat(messages.get(1).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(1).stateInfo().status(), is("Enter"));
		assertThat(messages.get(2).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(2).stateInfo().status(), is("Exit"));
		Map<String, String> data = data(persistence, "finished/201901/1.data");
		if (data.get("createstring").equals("Hello:Goodbye"))
			assertThat(data.get("joinresult"), is("Hi:Bye"));
		else assertThat(data.get("joinresult"), is("Bye:Hi"));
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
			return new Task(Default) {
				@Override
				public void execute() {
					put("CreateString", Math.random() < 0.5 ? "Hello:Goodbye" : "Goodbye:Hello");
					put("CreateString", Math.random() < 0.5 ? "Hello:Goodbye" : "Goodbye:Hello");
				}

			};
		}

		private Task processHello() {
			return new Task(Default) {

				@Override
				public void execute() {
					String[] result = get("CreateString").split(":");
					put("ProcessHello", result[0].equals("Hello") ? "0-Hi" : "1-Hi");
				}
			};
		}

		private Task processGoodbye() {
			return new Task(Default) {

				@Override
				public void execute() {
					String[] result = get("CreateString").split(":");
					put("ProcessGoodbye", result[0].equals("Goodbye") ? "0-Bye" : "1-Bye");
				}
			};
		}


		private Task joinResult() {
			return new Task(Default) {

				@Override
				public void execute() {
					String[] hello = get("ProcessHello").split("-");
					String[] goodbye = get("ProcessGoodbye").split("-");
					put("JoinResult", hello[0].equals("0") ? hello[1] + ":" + goodbye[1] : goodbye[1] + ":" + hello[1]);
				}
			};
		}

		@Override
		public String name() {
			return "StringContentReviewer";
		}
	}

}
