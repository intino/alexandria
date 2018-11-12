package io.intino.alexandria.inl;

import java.io.InputStream;
import java.time.Instant;
import java.util.List;

public class Event extends Message {
	static final String TS = "ts";
	private Message message;
	private Instant instant;

	Event(Message message) {
		super(message.type());
		this.message = message;
		this.instant = Instant.parse(get(TS));
	}

	@Override
	public boolean isEvent() {
		return true;
	}

	@Override
	public Event asEvent() {
		return this;
	}

	@Override
	public Message rename(String attribute, String newName) {
		return message.rename(attribute, newName);
	}

	@Override
	public Message remove(String attribute) {
		return message.remove(attribute);
	}

	@Override
	public List<Message> components() {
		return message.components();
	}

	@Override
	public List<Message> components(String type) {
		return message.components(type);
	}

	@Override
	public void add(Message component) {
		message.add(component);
	}

	@Override
	public void add(List<Message> components) {
		message.add(components);
	}

	@Override
	public void remove(Message component) {
		message.remove(component);
	}

	@Override
	public void remove(List<Message> components) {
		message.remove(components);
	}

	@Override
	public String toString() {
		return message.toString();
	}

	@Override
	public int length() {
		return message.length();
	}

	@Override
	public List<String> attributes() {
		return message.attributes();
	}

	@Override
	public Attachment attachment(String id) {
		return message.attachment(id);
	}

	@Override
	public boolean contains(String attribute) {
		return message.contains(attribute);
	}

	public Instant instant() {
		return instant;
	}

	@Override
	public String type() {
		return message.type();
	}

	@Override
	public boolean is(String type) {
		return message.is(type);
	}

	@Override
	public void type(String type) {
		message.type(type);
	}

	@Override
	public String get(String attribute) {
		return message.get(attribute);
	}

	@Override
	public Value read(String attribute) {
		return message.read(attribute);
	}

	@Override
	public Message set(String attribute, String value) {
		return message.set(attribute, value);
	}

	@Override
	public Message set(String attribute, Boolean value) {
		return message.set(attribute, value);
	}

	@Override
	public Message set(String attribute, Integer value) {
		return message.set(attribute, value);
	}

	@Override
	public Message set(String attribute, Double value) {
		return message.set(attribute, value);
	}

	@Override
	public Message attach(String attribute, String type, InputStream is) {
		return message.attach(attribute, type, is);
	}

	@Override
	public Message attach(String attribute, String id, String type, InputStream is) {
		return message.attach(attribute, id, type, is);
	}

	@Override
	public Message attach(String attribute, String type, byte[] content) {
		return message.attach(attribute, type, content);
	}

	@Override
	public Message attach(String attribute, String id, String type, byte[] content) {
		return message.attach(attribute, id, type, content);
	}

	@Override
	public Message append(String attribute, Boolean value) {
		return message.append(attribute, value);
	}

	@Override
	public Message append(String attribute, Integer value) {
		return message.append(attribute, value);
	}

	@Override
	public Message append(String attribute, Double value) {
		return message.append(attribute, value);
	}

	@Override
	public Message append(String attribute, String value) {
		return message.append(attribute, value);
	}

	@Override
	public List<Attachment> attachments() {
		return message.attachments();
	}
}
