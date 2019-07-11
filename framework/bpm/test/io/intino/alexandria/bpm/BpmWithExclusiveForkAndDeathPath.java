package io.intino.alexandria.bpm;

import io.intino.alexandria.inl.Message;
import org.junit.Test;

import java.util.List;

import static io.intino.alexandria.bpm.Link.Type.Exclusive;
import static io.intino.alexandria.bpm.Link.Type.Inclusive;
import static io.intino.alexandria.bpm.State.Type.Initial;
import static io.intino.alexandria.bpm.State.Type.Terminal;
import static io.intino.alexandria.bpm.Task.Type.Automatic;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BpmWithExclusiveForkAndDeathPath {

	@Test
	public void name() throws InterruptedException {
		MessageHub_ messageHub = new MessageHub_();
		Workflow workflow = new Workflow(messageHub, new ProcessFactory());
		messageHub.sendMessage("ProcessStatus", createProcessMessage());
		Process process = workflow.process("1");
		while (!hasEnded(process)) {
			if (process == null) process = workflow.process("1");
			Thread.sleep(100);
		}
		List<ProcessStatus> messages = process.messages();
		messages.stream().forEach(m -> {
			System.out.println(m.message().toString().replace("\n", "\t"));
		});
		assertThat(messages.get(0).message().toString(), is(createProcessMessage().toString()));
	}

	private boolean hasEnded(Process... processes) {
		for (Process process : processes) {
			if (process == null || process.processStatusList.isEmpty()) return false;
			if (process.messages().stream().noneMatch(m -> m.processStatus().equals("Exit"))) return false;
		}
		return true;
	}

	private Message createProcessMessage() {
		return new Message("ProcessStatus")
				.set("ts", "2019-01-01T00:00:00Z")
				.set("id", "1")
				.set("name", "StringContentReviewer")
				.set("status", "Enter");
	}

	static class StringContentReviewerProcess extends Process {

		protected StringContentReviewerProcess(String id) {
			super(id);
			addState(new State("CreateString", createString(), Initial));
			addState(new State("CheckContainsHello", checkContainsHelloTask()));
			addState(new State("ProcessHello", processHello()));
			addState(new State("ProcessHello2", processHello2()));
			addState(new State("ProcessGoodbye", processGoodbye()));
			addState(new State("ProcessGoodbye2", processGoodbye2()));
			addState(new State("Terminate", terminate(), Terminal));
			addLink(new Link("CreateString", "CheckContainsHello", Exclusive));
			addLink(new Link("CheckContainsHello", "ProcessHello", Exclusive));
			addLink(new Link("CheckContainsHello", "ProcessGoodbye", Exclusive));
			addLink(new Link("ProcessHello", "ProcessHello2", Inclusive));
			addLink(new Link("ProcessGoodbye", "ProcessGoodbye2", Inclusive));
			addLink(new Link("ProcessHello2", "Terminate", Inclusive));
			addLink(new Link("ProcessGoodbye2", "Terminate", Inclusive));
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

		private Task processHello2() {
			return new Task(Automatic) {

				@Override
				String execute() {
					return "Processing hello2";
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

		private Task terminate() {
			return new Task(Automatic) {

				@Override
				String execute() {
					return exitStateStatus("ProcessHello2").stateInfo().status().equals("Exit") ? "hello2" :
							exitStateStatus("ProcessGoodbye2").stateInfo().status().equals("Exit") ? "bye2" : "none";
				}
			};
		}

		private Task processGoodbye2() {
			return new Task(Automatic) {

				@Override
				String execute() {
					return "Processing goodbye2";
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
