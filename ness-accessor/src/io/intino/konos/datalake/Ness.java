package io.intino.konos.datalake;

import io.intino.konos.datalake.fs.FSDatalake;
import io.intino.konos.datalake.fs.FSTank;
import io.intino.konos.datalake.jms.JMSDatalake;
import io.intino.konos.datalake.jms.JMSTank;

import javax.jms.Session;
import java.time.Instant;

import static io.intino.konos.datalake.Datalake.Tank;

public class Ness {

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

	public Datalake.ReflowSession reflow(int blockSize, ReflowDispatcher dispatcher, Instant from) {
		return datalake.reflow(blockSize, dispatcher, from);
	}

	public Session session() {
		return datalake instanceof JMSDatalake ? ((JMSDatalake) datalake).session() : null;
	}

	public void commit() {
		datalake.commit();
	}

	public Tank add(String tank) {
		datalake.add(tank);
		return datalake instanceof JMSDatalake ? new JMSTank(tank, ((JMSDatalake) datalake)) : new FSTank(tank, (FSDatalake) datalake);
	}

}