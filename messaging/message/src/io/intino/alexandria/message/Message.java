package io.intino.alexandria.message;

import java.time.Instant;
import java.util.*;

public class Message {
	static final char listSeparator = '\u0001';
	private static final Value NullValue = new NullValue();
	private final Map<String, String> attributes;
	private String type;
	private Message owner;
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
		return contains(attribute) ? new DataValue(attributes.get(attribute)) : NullValue;
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
		attributes.put(attribute, value);
		return this;
	}

	public Message append(String attribute, Boolean value) {
		return append(attribute, value.toString());
	}

	public Message append(String attribute, Instant value) {
		return append(attribute, value.toString());
	}

	public Message append(String attribute, Long value) {
		return append(attribute, value.toString());
	}

	public Message append(String attribute, Integer value) {
		return append(attribute, value.toString());
	}

	public Message append(String attribute, Double value) {
		return append(attribute, value.toString());
	}

	public Message append(String attribute, String value) {
		String before = attributes.putIfAbsent(attribute, value);
		if (before != null) attributes.put(attribute, before + listSeparator + (value == null ? "\0" : value));
		return this;
	}

	public Message rename(String attribute, String newName) {
		attributes.put(newName, attributes.remove(attribute));
		return this;
	}

	public Message remove(String attribute) {
		attributes.remove(attribute);
		return this;
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
		for (Map.Entry<String, String> attribute : attributes.entrySet()) sb.append(stringOf(attribute)).append("\n");
		for (Message component : components()) sb.append("\n").append(component.toString());
		return sb.toString();
	}

	private String stringOf(Map.Entry<String, String> attribute) {
		return attribute.getKey() + ":" + (isMultiline(attribute.getValue()) ? indent(attribute.getValue()) : " " + attribute.getValue());
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
		return attributes.containsKey(attribute);
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


}