package io.intino.alexandria.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;


public class Message {
	static final String AttachmentHeader = "[&]";
	private static Value NullValue = new NullValue();
	private String type;
	private Message owner;
	private Map<String, Attribute> attributes;
	private List<Message> components;
	private Map<String, byte[]> attachments;

	public Message(String type) {
		this.type = type;
		this.owner = null;
		this.attributes = new LinkedHashMap<>();
		this.attachments = new HashMap<>();
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

	public List<String> attributes() {
		return new ArrayList<>(attributes.keySet());
	}

	public Value get(final String attribute) {
		return contains(attribute) ? new DataValue(use(attribute).value, attachments) : NullValue;
	}

	public Message set(String attribute, Boolean value) {
		return set(attribute, value.toString());
	}

	public Message set(String attribute, Instant value) {
		return set(attribute, value.toString());
	}

	public Message set(String attribute, Long value) {
		return set(attribute, value.toString());
	}

	public Message set(String attribute, Integer value) {
		return set(attribute, value.toString());
	}

	public Message set(String attribute, Double value) {
		return set(attribute, value.toString());
	}

	public Message set(String attribute, String value, InputStream is) {
		return set(attribute, value, contentOf(is));
	}

	public Message set(String attribute, String value, byte[] content) {
		return set(attribute, value + "@" + attach(content));
	}

	public Message set(String attribute, String value) {
		remove(attribute);
		use(attribute).value = value;
		return this;
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

	public Message append(String attribute, String name, InputStream is) {
		return append(attribute, name, contentOf(is));
	}

	public Message append(String attribute, String name, byte[] content) {
		return append(attribute, name + "@" + attach(content));
	}

	public Message rename(String attribute, String newName) {
		use(attribute).name = newName;
		add(use(attribute));
		remove(attribute);
		return this;
	}

	public Message remove(String attribute) {
		detach(use(attribute).value);
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

	private String attach(byte[] content) {
		String uuid = UUID.random();
		attachments.put(uuid, content);
		return uuid;
	}

	private void detach(String value) {
		if ((value == null) || (!value.contains("@"))) return;
		removeAttachments(value);
	}

	private void removeAttachments(String value) {
		for (String item : value.split("\n"))
			removeAttachment(uuidIn(item));
	}

	private String uuidIn(String item) {
		return item.substring(item.indexOf('@') + 1);
	}

	private void removeAttachment(String uuid) {
		if (UUID.is(uuid)) attachments.remove(uuid);
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
		component.attachments = attachments;
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
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(qualifiedType()).append("]\n");
		for (Attribute attribute : attributes.values()) sb.append(stringOf(attribute)).append("\n");
		for (Message component : components()) sb.append("\n").append(component.toString());
		if (hasAttachments()) sb.append("\n" + AttachmentHeader + "\n");
		return sb.toString();
	}

	boolean hasAttachments() {
		return (owner == null) && (attachments.size() > 0);
	}

	Map<String, byte[]> allAttachments() {
		return attachments;
	}

	private String stringOf(Attribute attribute) {
		return attribute.name + ":" + (isMultiline(attribute.value) ? indent(attribute.value) : " " + attribute.value);
	}

	private boolean isMultiline(String value) {
		return value != null && value.contains("\n");
	}

	private String qualifiedType() {
		return owner != null ? owner.qualifiedType() + "." + type : type;
	}

	public int length() {
		return toString().length();
	}

	public boolean contains(String attribute) {
		return attributes.containsKey(attribute.toLowerCase());
	}

	public List<String> attachments() {
		return new ArrayList<>(attachments.keySet());
	}

	public byte[] attachment(String id) {
		return attachments.getOrDefault(id, new byte[0]);
	}

	Message put(Map<String, byte[]> attachments) {
		this.attachments = attachments;
		return this;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		Message message = (Message) object;
		return Objects.equals(type, message.type) &&
				attributes.keySet().stream().allMatch(k -> attributeEquals(message, k)) &&
				attachments.keySet().stream().allMatch(k -> attachmentEquals(message, k)) &&
				Objects.equals(components, message.components);
	}

	private boolean attributeEquals(Message message, String key) {
		return message.get(key).data().equals(get(key).data());
	}

	private boolean attachmentEquals(Message message, String key) {
		return Arrays.equals(message.attachments.get(key), message.attachments.get(key));
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, owner, attributes, components, attachments);
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

	public interface Value {
		String data();

		<T> T as(Class<T> type);

		Instant asInstant();

		int asInteger();

		Long asLong();

		String asString();

		double asDouble();

		boolean asBoolean();
	}

	static class Attribute {
		String name;
		String value;

		Attribute(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name + ":" + value;
		}
	}

	private static class UUID {
		private static int[] HyphenPositions = new int[]{8, 13, 18, 23};

		static boolean is(String str) {
			for (int n : HyphenPositions)
				if (str.charAt(n) != '-') return false;
			return true;
		}

		static String random() {
			return java.util.UUID.randomUUID().toString();
		}
	}
}