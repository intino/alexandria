package io.intino.alexandria.inl;


import com.sun.xml.internal.ws.api.message.Attachment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static io.intino.alexandria.inl.Event.TS;

public class Message {
	private String type;
	private Message owner;
	private Event event;
	private Map<String, Attribute> attributes;
	private List<Message> components;
	private List<Attachment> attachments;

	public Message(String type) {
		this(type, null);
	}

	public Message(String type, Message owner) {
		this.type = type;
		this.owner = owner;
		this.attributes = new LinkedHashMap<>();
		this.attachments = null;
		this.components = null;
	}

	public String type() {
		return type;
	}

	public boolean is(String type) {
		return type.equalsIgnoreCase(this.type);
	}

	public void type(String type) {
		this.type = type;
	}

	public String get(String attribute) {
		return use(attribute).value;
	}

	public Value read(final String attribute) {
		return new Value() {
			@Override
			@SuppressWarnings("unchecked")
			public <T> T as(Class<T> type) {
				String value = use(attribute).value;
				return value != null ? (T) InlParsers.get(type).parse(value) : null;
			}
		};
	}

	public Message set(String attribute, String value) {
		if (value == null) return remove(attribute);
		use(attribute).value = value;
		return this;
	}

	public Message set(String attribute, Boolean value) {
		return set(attribute, value.toString());
	}

	public Message set(String attribute, Integer value) {
		return set(attribute, value.toString());
	}

	public Message set(String attribute, Double value) {
		return set(attribute, value.toString());
	}

	public Message attach(String attribute, String type, InputStream is) {
		return attach(attribute, randomId(), type, contentOf(is));
	}

	public Message attach(String attribute, String id, String type, InputStream is) {
		return attach(attribute, id, type, contentOf(is));
	}

	public Message attach(String attribute, String type, byte[] content) {
		return attach(attribute, randomId(), type, content);
	}

	public Message attach(String attribute, String id, String type, byte[] content) {
		return append(use(attribute), "@" + putAttachment(id + "." + type, content));
	}

	public Message append(String attribute, Boolean value) {
		return append(use(attribute), value.toString());
	}

	public Message append(String attribute, Integer value) {
		return append(use(attribute), value.toString());
	}

	public Message append(String attribute, Double value) {
		return append(use(attribute), value.toString());
	}

	public Message append(String attribute, String value) {
		return append(use(attribute), value);
	}

	private Message append(Attribute attribute, String value) {
		attribute.value =
				(attribute.value == null ? "" : attribute.value + "\n") +
				(value == null ? "\0" : value);
		return this;
	}

	public List<Attachment> attachments() {
		return attachments != null ? attachments : (attachments = new ArrayList<>());
	}

	private String randomId() {
		return UUID.randomUUID().toString();
	}

	private String putAttachment(String id, byte[] bytes) {
		attachments().add(new Attachment(id, bytes));
		return id;
	}

	private void detach(String id) {
		if (id.contains("@")) attachments().remove(id.substring(1));
	}



	public boolean isEvent() {
		return this.attributes.containsKey(TS);
	}

	public Event asEvent() {
		return isEvent() ? event() : null;
	}

	private static String indent(String text) {
		return "\n\t" + text.replaceAll("\\n", "\n\t");
	}

	private static byte[] contentOf(InputStream is) {
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			int length;
			byte[] data = new byte[16384];
			while ((length = is.read(data, 0, data.length)) != -1)
				os.write(data, 0, length);
			os.flush();
			return os.toByteArray();

		} catch (IOException e) {
			return new byte[0];
		}
	}

	private Event event() {
		return event != null ? event : (event = new Event(this));
	}

	public Message rename(String attribute, String newName) {
		use(attribute).name = newName;
		add(use(attribute));
		remove(attribute);
		return this;
	}

	public Message remove(String attribute) {
		attributes.remove(attribute.toLowerCase());
		return this;
	}

	private Attribute use(String attribute) {
		if (!contains(attribute)) add(new Attribute(attribute));
		return attributes.get(attribute.toLowerCase());
	}

	private void add(Attribute attribute) {
		attributes.put(attribute.name.toLowerCase(), attribute);
	}

	public List<Message> components() {
		return components == null ? new ArrayList<>() : new ArrayList<>(components);
	}

	public List<Message> components(String type) {
		List<Message> result = new ArrayList<>();
		if (components == null) return result;
		for (Message component : components)
			if (component.is(type)) result.add(component);
		return result;
	}

	public void add(Message component) {
		if (components == null) components = new ArrayList<>();
		components.add(component);
		component.owner = this;
	}

	public void add(List<Message> components) {
		if (components == null) return;
		for (Message component : components) add(component);
	}

	public void remove(Message component) {
		components.remove(component);
	}

	public void remove(List<Message> components) {
		this.components.removeAll(components);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("[" + path() + "]\n");
		for (Attribute attribute : attributes.values()) result.append(stringOf(attribute)).append("\n");
		for (Message component : components()) result.append("\n").append(component.toString());
		return result.toString();
	}

	private String stringOf(Attribute attribute) {
		return attribute.name + ":" + (isMultiline(attribute.value) ? indent(attribute.value) : " " + attribute.value);
	}

	private boolean isMultiline(String value) {
		return value != null && value.contains("\n");
	}

	private String path() {
		return owner != null ? owner.path() + "." + type : type;
	}

	public int length() {
		return toString().length();
	}

	public List<String> attributes() {
		return new ArrayList<>(attributes.keySet());
	}

	public Attachment attachment(String id) {
		if (id.startsWith("@")) id = id.substring(1);
		for (Attachment a : attachments()) {
			if (a.id().equals(id)) {
				return a;
			}
		}
		return null;
	}

	public boolean contains(String attribute) {
		return attributes.containsKey(attribute.toLowerCase());
	}

	public interface Value {

		<T> T as(Class<T> type);

	}

	static class Attribute {
		String name;
		String value;

		Attribute(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name + ": " + value;
		}


	}

	public static class Attachment {
		private String id;

		private byte[] data;

		private Attachment(String id, byte[] data) {
			this.id = id;
			this.data = data;
		}

		public String id() {
			return id;
		}

		public String type() {
			return id.substring(id.lastIndexOf('.') + 1);
		}

		public byte[] data() {
			return data;
		}

		public void data(InputStream is) {
			data(Message.contentOf(is));
		}

		public void data(byte[] data) {
			this.data = data;
		}
	}
}

