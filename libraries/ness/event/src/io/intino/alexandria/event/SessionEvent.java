package io.intino.alexandria.event;

import java.time.Instant;
import java.util.UUID;

public class SessionEvent extends Event {
	public SessionEvent() {
		super("Session");
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
