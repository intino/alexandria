import io.intino.konos.datalake.Datalake;
import io.intino.konos.datalake.Ness;
import io.intino.ness.inl.Message;
import org.junit.Test;

import java.time.Instant;

public class NessWrapper {

	@Test
	public void shouldStartNess() throws Exception {
		final Ness ness = new Ness("failover:(tcp://localhost:61616)", "ness", "ness", "ness");
//		ness.start();
		final Datalake.Tank tank = ness.add("");
		Thread.sleep(10000);
	}


	@Test
	public void sendAttachment() {
		final Ness ness = new Ness("file://./temp/attach", "user", "password", "");
		final Datalake.Tank tank = ness.add("happysense.dialog");
		final Message message = new Message("dialog").set("name", "dialog1");
		message.set("ts", Instant.now().toString());
		message.set("value", "txt", "example".getBytes());
		tank.drop(message);
	}
}
