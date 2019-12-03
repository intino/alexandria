import io.intino.alexandria.event.Event;
import io.intino.alexandria.messagehub.JmsEventHub;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.Objects;

public class JmsTerminalTest {

	@Test
	@Ignore
	public void testMessageOutBox() throws InterruptedException {
		JmsEventHub eventHub = new JmsEventHub("failover:(tcp://localhost:63000)", "user1", "1234", "", new File("outBox"));
		while (true) {
			eventHub.sendEvent("lalala", new TestEvent("tt").field1("v1"));
			Thread.sleep(10000);
		}
	}

	@Test
	@Ignore
	public void testPutAndHandle() throws InterruptedException {
		JmsEventHub eventHub = new JmsEventHub("failover:(tcp://localhost:63000)", "cobranza", "gy7a144hjv", "cobranza", new File("outBox"));
		new Thread(() -> eventHub.attachListener("lalala", m -> System.out.println(m.toString()))).start();

		while (true) {
			eventHub.sendEvent("lalala", new TestEvent("tt").field1("v1"));
			Thread.sleep(10000);
		}
	}

	@After
	public void tearDown() throws Exception {
		for (File outBox : Objects.requireNonNull(new File("outBox").listFiles())) Files.delete(outBox.toPath());
	}

	public static class TestEvent extends Event {

		public TestEvent(String type) {
			super(type);
		}

		public String field1() {
			return message.get("field1").asString();
		}

		public TestEvent field1(String value) {
			message.set("field1", value);
			return this;
		}
	}
}
