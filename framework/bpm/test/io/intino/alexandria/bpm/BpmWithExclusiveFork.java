package io.intino.alexandria.bpm;

import io.intino.alexandria.message.Message;
import org.junit.Test;

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
		MessageHub messageHub = new MessageHub_();
		Workflow workflow = new Workflow(messageHub, (id, name) -> new StringContentReviewerProcess(id));
		messageHub.sendMessage("ProcessStatus", createProcessMessage());
		Process process = waitForProcess(workflow);
		List<ProcessStatus> messages = process.messages();
		assertThat(messages.get(0).processStatus(), is("Enter"));
		assertThat(messages.get(1).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(1).stateInfo().status(), is("Enter"));
		assertThat(messages.get(2).stateInfo().name(), is("CreateString"));
		assertThat(messages.get(2).stateInfo().status(), is("Exit"));
		assertThat(messages.get(3).stateInfo().name(), is("CheckContainsHello"));
		assertThat(messages.get(3).stateInfo().status(), is("Enter"));
		assertThat(messages.get(4).stateInfo().name(), is("CheckContainsHello"));
		assertThat(messages.get(4).stateInfo().status(), is("Exit"));
		if(process.exitStateStatus("CreateString").taskInfo().result().equals("Hello")){
			assertThat(process.exitStateStatus("ProcessHello").taskInfo().result(), is("Processing hello"));
			assertThat(process.exitStateStatus("ProcessGoodbye").stateInfo().status(), is("Rejected"));
		}
		else{
			assertThat(process.exitStateStatus("ProcessGoodbye").taskInfo().result(), is("Processing goodbye"));
			assertThat(process.exitStateStatus("ProcessHello").stateInfo().status(), is("Rejected"));
		}
	}

	public Process waitForProcess(Workflow workflow) throws InterruptedException {
		Process process = workflow.process("1");
		while (!hasEnded(process)) {
			if (process == null) process = workflow.process("1");
			Thread.sleep(100);
		}
		Thread.sleep(100);
		return process;
	}

	private boolean hasEnded(Process... processes) {
		for (Process process : processes) {
			if (process == null || process.processStatusList.isEmpty()) return false;
			if (process.messages().stream().noneMatch(m -> m.processStatus().equals("Exit"))) return false;
		}
		return true;
	}

	private Message createProcessMessage() {
		return new ProcessStatus("1", "StringContentReviewer", "Enter").message();
	}

	static class StringContentReviewerProcess extends Process {

		StringContentReviewerProcess(String id) {
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

}
