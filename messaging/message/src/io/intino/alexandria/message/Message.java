package io.intino.alexandria.message;

import javax.xml.namespace.QName;
import java.lang.reflect.Array;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Message {
	public static final char ListSep = '\u0001';
	public static final String ListSepStr = String.valueOf(ListSep);
	public static final String NullValue = "\0";

	private final Map<String, String> attributes;
	private String type;
	private Message owner;
	private List<Message> components;

	public Message(String type) {
		this.type = type;
		this.attributes = new LinkedHashMap<>();
	}

	public String type() {
		return type;
	}

	public void type(String type) {
		this.type = type;
	}

	public boolean is(String type) {
		return type.equalsIgnoreCase(this.type);
	}

	public boolean isComponent() {
		return type.contains(".");
	}

	public boolean isComponentOf(String type) {
		return isComponent() && this.type.startsWith(type);
	}

	public List<String> attributes() {
		return new ArrayList<>(attributes.keySet());
	}

	public boolean contains(String attribute) {
		return attributes.containsKey(attribute);
	}

	public Value get(String attribute) {
		return contains(attribute) ? new DataValue(normalize(attributes.get(attribute))) : Value.Null;
	}

	public Message set(String attribute, Object value) {
		if(value == null) return set(attribute, NullValue); // TODO: check
		if(isIterable(value.getClass())) return setIterable(attribute, value);
		checkElementTypeIsSupported(value);
		return set(attribute, str(value));
	}

	private Message setIterable(String attribute, Object value) {
		attributes.put(attribute, serializedListFromIterable(value));
		return this;
	}

	public Message set(String attribute, String value) {
		if(value == null)
			attributes.put(attribute, NullValue);
		else
			attributes.put(attribute, value);
		return this;
	}

	public Message append(String attribute, Object value) {
		if(value == null) return append(attribute, NullValue);
		if(isIterable(value.getClass())) return appendIterable(attribute, value);
		checkElementTypeIsSupported(value);
		return append(attribute, str(value));
	}

	private Message appendIterable(String attribute, Object value) {
		return append(attribute, serializedListFromIterable(value));
	}

	public Message append(String attribute, String value) {
		if(!contains(attribute)) return set(attribute, value);
		String oldValue = attributes.putIfAbsent(attribute, value);
		attributes.put(attribute, oldValue + ListSep + (value == null ? NullValue : value));
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

	private static final String ListSepStr2 = ListSepStr + ListSepStr;
	public Message remove(String attribute, Object value) {
		String str = value == null ? NullValue : str(value);
		attributes.computeIfPresent(attribute, (k, v) -> v.replace(str, "").replace(ListSepStr2, ListSepStr));
		return this;
	}

	public List<Message> components() {
		return components == null ? new ArrayList<>(0) : new ArrayList<>(components);
	}

	public List<Message> components(String type) {
		return components == null
				? new ArrayList<>(0)
				: components.stream().filter(c -> c.is(type)).collect(Collectors.toList());
	}

	public void add(Message component) {
		if(component == null) throw new NullPointerException("Component cannot be null");
		if (components == null) components = new ArrayList<>();
		components.add(component);
		component.owner = this;
	}

	public void add(List<Message> components) {
		if (components == null) return;
		if(this.components == null) this.components = new ArrayList<>(components.size());
		components.forEach(this::add);
	}

	public void remove(Message component) {
		if(component == null) return;
		components.remove(component);
	}

	public void remove(List<Message> components) {
		if(components == null) return;
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

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		Message message = (Message) object;
		return Objects.equals(type, message.type) &&
				attributes.keySet().stream().allMatch(k -> attributeEquals(message, k)) &&
				Objects.equals(components, message.components);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, attributes, components);
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

	private boolean isIterable(Class<?> type) {
		return Iterable.class.isAssignableFrom(type) || type.isArray();
	}

	private Iterator<?> iteratorOf(Object value) {
		if(value instanceof Iterator) return (Iterator<?>) value;
		if(value instanceof Iterable) return ((Iterable<?>) value).iterator();
		return iteratorFromArray(value);
	}

	private Iterator<?> iteratorFromArray(Object array) {
		return new Iterator<>() {
			private final int length = Array.getLength(array);
			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < length;
			}

			@Override
			public Object next() {
				return Array.get(array, index++);
			}
		};
	}

	private String serializedListFromIterable(Object value) {
		StringBuilder list = new StringBuilder();
		Iterator<?> iterator = iteratorOf(value);
		while(iterator.hasNext()) {
			Object item = iterator.next();
			checkElementTypeIsSupported(item);
			list.append(item).append(ListSep);
		}
		return list.toString();
	}

	private void checkElementTypeIsSupported(Object value) {
		Class<?> clazz = value.getClass();
		if(clazz.isArray()) throw new IllegalArgumentException("Message does not support multidimensional arrays values");
		if(Parser.of(clazz) == null) throw new IllegalArgumentException("Message does not support values of type " + clazz);
	}

	private String str(Object value) {
		return String.valueOf(value);
	}

	private String normalize(String value) {
		return Objects.equals(value, NullValue) ? null : value;
	}

	private boolean attributeEquals(Message message, String key) {
		return message.contains(key) && Objects.equals(message.get(key).data(), get(key).data());
	}

	private static String indent(String text) {
		return "\n\t" + text.replaceAll("\\n", "\n\t");
	}

	public interface Value {

		Value Null = new NullValue();

		boolean isEmpty();

		String data();

		<T> T as(Class<T> type);

		Instant asInstant();

		int asInteger();

		Long asLong();

		String asString();

		double asDouble();

		boolean asBoolean();

		List<Value[]> asTable();
	}
}