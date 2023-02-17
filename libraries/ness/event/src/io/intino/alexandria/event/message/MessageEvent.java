package io.intino.alexandria.event.message;

import io.intino.alexandria.event.Event;
import io.intino.alexandria.message.Message;

import java.time.Instant;

public class MessageEvent implements Event {
	private static final String TS = "ts";
	private static final String SS = "ss";
	protected Message message;
	protected Instant ts;
	protected String ss;

	public MessageEvent(String type) {
		this.message = new Message(type);
		message.set(TS, this.ts = Instant.now());
	}

	public MessageEvent(Message message) {
		this.message = message;
		this.ts = message.get(TS).asInstant();
		this.ss = message.get(SS).asString();
	}

	public Instant ts() {
		return ts;
	}

	public String ss() {
		return ss;
	}

	public MessageEvent ts(Instant ts) {
		this.message.set(TS, this.ts = ts);
		return this;
	}

	public MessageEvent ss(String ss) {
		this.message.set(SS, this.ss = ss);
		return this;
	}

	public Message toMessage() {
		return message;
	}

	@Override
	public String toString() {
		return message.toString();
	}
}
