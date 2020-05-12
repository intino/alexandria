package io.intino.alexandria.event;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;

import java.io.InputStream;
import java.time.Instant;
import java.util.List;
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

	public Message.Value get(String attribute) {
		return message.get(attribute);
	}

	public SessionEvent set(String attribute, Boolean value) {
		message.set(attribute, value);
		return this;
	}

	public SessionEvent set(String attribute, Instant value) {
		message.set(attribute, value);
		return this;
	}

	public SessionEvent set(String attribute, Long value) {
		message.set(attribute, value);
		return this;
	}

	public SessionEvent set(String attribute, Integer value) {
		message.set(attribute, value);
		return this;
	}

	public SessionEvent set(String attribute, Double value) {
		message.set(attribute, value);
		return this;
	}

	public SessionEvent set(String attribute, String value) {
		message.set(attribute, value);
		return this;
	}

	public boolean contains(String attribute) {
		return message.contains(attribute);
	}

	public List<String> attributes() {
		return message.attributes();
	}

	public List<Message> components() {
		return message.components();
	}

	public List<Message> components(String type) {
		return message.components(type);
	}

	public void add(Message component) {
		message.add(component);
	}

	public SessionEvent ts(Instant ts) {
		return this;
	}

	public SessionEvent ss(String ss) {
		return this;
	}
}
