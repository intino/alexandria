import io.intino.alexandria.nessaccesor.NessAccessor;
import io.intino.alexandria.nessaccesor.jms.JMSEventStore;
import io.intino.ness.inl.Message;
import org.junit.Test;

import java.time.Instant;

public class NessWrapper {

	@Test
	public void shouldStartNess() throws Exception {
		final NessAccessor nessAccessor = new NessAccessor("failover:(tcp://localhost:63000)", "feeder", "UHH7G+Nszp", "feeder-adq");
		nessAccessor.connect("Transacted");
		final NessAccessor.EventStore.Tank tank = nessAccessor.eventStore().add("adquiver.advertiser");
		tank.put(new Message("advertiser").set("ts", Instant.now().toString()));
		((JMSEventStore) nessAccessor.eventStore()).session().commit();
		Thread.sleep(10000);
	}


	@Test
	public void sendAttachment() {
		final NessAccessor nessAccessor = new NessAccessor("file://./temp/../attach", "user", "password", "");
		final NessAccessor.EventStore.Tank tank = nessAccessor.eventStore().add("happysense.dialog");
		final Message message = new Message("dialog").set("name", "dialog1");
		message.set("ts", Instant.now().toString());
		message.set("value", "txt", "example".getBytes());
		tank.put(message);
	}
}
