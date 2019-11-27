import io.intino.alexandria.message.Message;
import io.intino.alexandria.messagehub.JmsMessageHub;
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
		JmsMessageHub messageHub = new JmsMessageHub("failover:(tcp://localhost:63000)", "user1", "1234", "", new File("outBox"));
		while (true) {
			messageHub.sendMessage("lalala", new Message("tt").append("a1", "v1"));
			Thread.sleep(10000);
		}
	}

	@Test
	@Ignore
	public void testPutAndHandle() throws InterruptedException {
		JmsMessageHub messageHub = new JmsMessageHub("failover:(tcp://localhost:63000)", "cobranza", "gy7a144hjv", "cobranza", new File("outBox"));
		new Thread(() -> messageHub.attachListener("lalala", m -> System.out.println(m.toString()))).start();

		while (true) {
			messageHub.sendMessage("lalala", new Message("tt").append("a1", "v1"));
			Thread.sleep(10000);
		}

	}


	@After
	public void tearDown() throws Exception {
		for (File outBox : Objects.requireNonNull(new File("outBox").listFiles())) Files.delete(outBox.toPath());
	}
}
