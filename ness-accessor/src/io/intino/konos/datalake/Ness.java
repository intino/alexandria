package io.intino.konos.datalake;

import io.intino.konos.datalake.EventDatalake.User;
import io.intino.konos.datalake.fs.FSEventDatalake;
import io.intino.konos.datalake.jms.JMSEventDatalake;
import io.intino.sezzet.SetStore;
import io.intino.sezzet.SezzetStore;

import java.util.List;

import static io.intino.konos.datalake.Helper.*;

public class Ness {

	private final EventDatalake eventDatalake;
	private final SetStore setDatalake;

	public Ness(String url, String user, String password, String clientID) {
		this.eventDatalake = (url.startsWith("file://")) ? new FSEventDatalake(eventDatalakeDirectory(url), scaleOf(url)) : new JMSEventDatalake(url, user, password, clientID);
		setDatalake = (url.startsWith("file://")) ? new SezzetStore(setDatalakeDirectory(url), setScaleOf(url)) : null;
	}

	public void connect(String... args) {
		eventDatalake.connect(args);
	}

	public void disconnect() {
		eventDatalake.disconnect();
	}

	public boolean isConnected() {
		return eventDatalake.isConnected();
	}

	public EventDatalake eventDatalake() {
		return eventDatalake;
	}

	public SetStore setDatalake() {
		return setDatalake;
	}

	public List<User> users() {
		return eventDatalake.users();
	}

}