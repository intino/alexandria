package io.intino.alexandria.event;

import io.intino.alexandria.message.Message;

import java.time.Instant;

public class SessionEvent extends Event {
	protected Message message;

	public SessionEvent(String type) {
		super(type);
	}

	public SessionEvent(Message message) {
		super(message);
	}

	public String ss() {
		return "";
	}

	public SessionEvent ts(Instant ts) {
		super.ts(ts);
		return this;
	}

	public SessionEvent ss(String ss) {
		return this;
	}

}
