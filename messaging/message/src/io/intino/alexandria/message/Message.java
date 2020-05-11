package io.intino.alexandria.message;

import java.time.Instant;
import java.util.*;

public class Message {
	private static final Value NullValue = new NullValue();
	private String type;
	private Message owner;
	private final Map<String, Attribute> attributes;
	private List<Message> components;

	public Message(String type) {
		this.type = type;
		this.owner = null;
		this.attributes = new LinkedHashMap<>();
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
		return contains(attribute) ? new DataValue(use(attribute).value) : NullValue;
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

	public Message set(String attribute, String value) {
		remove(attribute);
		use(attribute).value = value;
		return this;
	}

	public Message append(String attribute, Boolean value) {
		return append(use(attribute), value.toString());
	}

	public Message append(String attribute, Instant value) {
		return append(use(attribute), value.toString());
	}

	public Message append(String attribute, Long value) {
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
		attribute.value = (attribute.value == null ? "" : attribute.value + "\n") + (value == null ? "\0" : value);
		return this;
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

	public Message remove(String attribute, Object value) {
		Attribute attr = attributes.get(attribute.toLowerCase());
		if (attr == null) return this;
		attr.value = attr.value.replace(value.toString() + "\n", "");
		if (attr.value.isEmpty()) remove(attribute);
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
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(qualifiedType()).append("]\n");
		for (Attribute attribute : attributes.values()) sb.append(stringOf(attribute)).append("\n");
		for (Message component : components()) sb.append("\n").append(component.toString());
		return sb.toString();
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

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		Message message = (Message) object;
		return Objects.equals(type, message.type) &&
				attributes.keySet().stream().allMatch(k -> attributeEquals(message, k)) &&
				Objects.equals(components, message.components);
	}

	private boolean attributeEquals(Message message, String key) {
		return message.contains(key) && message.get(key).data().equals(get(key).data());
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, owner, attributes, components);
	}

	private static String indent(String text) {
		return "\n\t" + text.replaceAll("\\n", "\n\t");
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
		private static final int[] HyphenPositions = new int[]{8, 13, 18, 23};

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