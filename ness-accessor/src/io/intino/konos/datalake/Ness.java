package io.intino.konos.datalake;

import io.intino.konos.datalake.fs.FSDatalake;
import io.intino.konos.datalake.fs.FSTank;
import io.intino.konos.datalake.jms.JMSDatalake;
import io.intino.konos.datalake.jms.JMSTank;
import io.intino.konos.jms.Consumer;
import io.intino.ness.inl.Message;

import java.time.Instant;

import static io.intino.konos.datalake.Datalake.Tank;

public class Ness {

	private Instant lastMessage;
	private int receivedMessages = 0;
	private final Datalake datalake;

	public Ness(String url, String user, String password, String clientID) {
		this.datalake = (url.startsWith("file://")) ? new FSDatalake(url) : new JMSDatalake(url, user, password, clientID);
	}

	public void connect(String... args) {
		datalake.connect(args);
	}

	public void disconnect() {
		datalake.disconnect();
	}

	public Datalake.ReflowSession reflow(int blockSize, MessageDispatcher dispatcher, Instant from, Tank... tanks) {
		return datalake.reflow(blockSize, dispatcher, from, tanks);
	}

	public void commit() {
		datalake.commit();
	}

	public Tank add(String tank) {
		datalake.add(tank);
		return datalake instanceof JMSDatalake ? new JMSTank(tank, ((JMSDatalake) datalake)) : new FSTank(tank, (FSDatalake) datalake);
	}

	public Instant lastMessage() {
		return lastMessage;
	}

	public void lastMessage(Instant lastMessage) {
		this.lastMessage = lastMessage;
		if (lastMessage != null) this.receivedMessages++;
	}

	public int receivedMessages() {
		return receivedMessages;
	}

	public void reset() {
		this.receivedMessages = 0;
	}

	public interface TankFlow extends Consumer {
		void consume(Message message);
	}


}
