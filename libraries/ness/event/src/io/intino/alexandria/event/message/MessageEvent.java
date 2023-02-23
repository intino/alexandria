package io.intino.alexandria.event.message;

import io.intino.alexandria.event.Event;
import io.intino.alexandria.message.Message;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

public class MessageEvent implements Event {
	private static final String TS = "ts";
	private static final String SS = "ss";

	protected Message message;
	protected Instant ts;
	protected String ss;

	public MessageEvent(String type, String ss) {
		this.message = new Message(type);
		message.set(TS, this.ts = Instant.now());
		message.set(SS, this.ss = requireNonNull(ss));
	}

	public MessageEvent(Message message) {
		this.message = message;
		this.ts = requireNonNull(message.get(TS).asInstant());
		this.ss = requireNonNull(message.get(SS).asString());
	}

	public Instant ts() {
		return ts;
	}

	public String ss() {
		return ss;
	}

	@Override
	public Format format() {
		return Format.Message;
	}

	public MessageEvent ts(Instant ts) {
		this.message.set(TS, this.ts = requireNonNull(ts));
		return this;
	}

	public MessageEvent ss(String ss) {
		this.message.set(SS, this.ss = requireNonNull(ss));
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
