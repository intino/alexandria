package io.intino.alexandria.message;

import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class Event extends Message {
	static final String TS = "ts";
	private Message message;
	private Instant instant;

	Event(Message message) {
		super(message.type());
		this.message = message;
		this.instant = Instant.parse(get(TS).toString());
	}

	@Override
	public boolean isEvent() {
		return true;
	}

	@Override
	public Event asEvent() {
		return this;
	}

	public Instant instant() {
		return instant;
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
	public boolean hasAttachments() {
		return message.hasAttachments();
	}

	@Override
	public Map<String, byte[]> allAttachments() {
		return message.allAttachments();
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
	public boolean contains(String attribute) {
		return message.contains(attribute);
	}

	@Override
	public List<String> attachments() {
		return message.attachments();
	}

	@Override
	public byte[] attachment(String id) {
		return message.attachment(id);
	}

	@Override
	public Message put(Map<String, byte[]> attachments) {
		return message.put(attachments);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		return message.equals(object);
	}

	@Override
	public int hashCode() {
		return message.hashCode();
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
	public Value get(String attribute) {
		return message.get(attribute);
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
	public Message set(String attribute, String value, InputStream is) {
		return message.set(attribute, value, is);
	}

	@Override
	public Message set(String attribute, String value, byte[] content) {
		return message.set(attribute, value, content);
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
	public Message append(String attribute, String name, InputStream is) {
		return message.append(attribute, name, is);
	}

	@Override
	public Message append(String attribute, String name, byte[] content) {
		return message.append(attribute, name, content);
	}

}
