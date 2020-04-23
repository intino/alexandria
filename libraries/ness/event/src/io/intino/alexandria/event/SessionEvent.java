package io.intino.alexandria.event;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;

import java.time.Instant;
import java.util.UUID;

public class SessionEvent extends Event {
	public static final String PATH = "Session";

	public SessionEvent(Message message) {
		super(message.type().equals(PATH) ? message : new Message(PATH));
		if (!message.type().equals(PATH)) Logger.error("Not Session Event on constructor");
	}

	public SessionEvent() {
		super(PATH);
		ts = Instant.now();
		ss = UUID.randomUUID() + "@" + ts.toString();
	}

	public SessionEvent ts(Instant ts) {
		return this;
	}

	public SessionEvent ss(String ss) {
		return this;
	}
}
