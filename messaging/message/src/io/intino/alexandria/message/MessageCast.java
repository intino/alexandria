package io.intino.alexandria.message;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static java.util.Collections.emptyList;


public class MessageCast {
	private static final Map<Class<?>, String> classNames = new HashMap<>();
	private static final Map<String, Field> fields = new HashMap<>();
	private final Message message;

	private MessageCast(Message message) {
		this.message = message;
	}

	private <T> Object fillObject(Message message, Class<T> aClass, Object object) throws IllegalAccessException {
		fillAttributes(message, aClass, object);
		fillComponents(message, aClass, object);
		return object;
	}

	private <T> void fillAttributes(Message message, Class<T> aClass, Object object) throws IllegalAccessException {
		for (String attribute : message.attributes()) {
			Field field = fieldByName(aClass, attribute);
			if (field == null) continue;
			setField(field, object, valueOf(message, attribute, field));
		}
	}

	private Object valueOf(Message message, String attribute, Field field) {
		return parserOf(field).parse(message.get(attribute).toString());
	}

	@SuppressWarnings({"unchecked"})
	private <T> void fillComponents(Message message, Class<T> aClass, Object object) throws IllegalAccessException {
		for (Message component : message.components()) {
			Field field = fieldByName(aClass, component.type());
			if (field != null)
				setField(field, object, fillObject(component, classOf(field), create(classOf(field))));
		}
	}

	private void setField(Field field, Object owner, Object value) throws IllegalAccessException {
		field.setAccessible(true);
		if (isEnum(field))
			field.set(owner, Enum.valueOf(field.getType().asSubclass(Enum.class), (String) value));
		else if (isList(field))
			field.set(owner, value instanceof List ? append((List) field.get(owner), (List) value) : append((List) field.get(owner), value));
		else if (isArray(field))
			field.set(owner, append((Object[]) field.get(owner), (Object[]) value));
		else
			field.set(owner, value);
	}

	private Parser parserOf(Field field) {
		return isList(field) ?
				parserOf(field.getGenericType().toString()) :
				parserOf(isEnum(field) ? String.class : field.getType());
	}

	private boolean isEnum(Field field) {
		return field.getType().isEnum();
	}

	private boolean isArray(Field field) {
		return field.getType().isArray();
	}

	private boolean isList(Field field) {
		return field.getType().isAssignableFrom(List.class);
	}

	private Parser parserOf(Class<?> type) {
		return Parser.of(type);
	}

	private Parser parserOf(final String name) {
		return new Parser() {
			final Parser parser = Parser.of(arrayClass());

			private Class<?> arrayClass() {
				try {
					String className = "[L" + name.substring(name.indexOf('<') + 1).replace(">", "") + ";";
					return Class.forName(className);
				} catch (ClassNotFoundException e) {
					return null;
				}
			}

			@Override
			public Object parse(String text) {
				Object[] array = (Object[]) parser.parse(text);
				return array != null ? Arrays.asList(array) : emptyList();
			}
		};
	}

	@SuppressWarnings("unchecked")
	public <T> T as(Class<T> aClass) throws IllegalAccessException {
		return message != null ? (T) fillObject(message, aClass, create(aClass)) : null;
	}

	public static MessageCast cast(Message message) {
		return new MessageCast(message);
	}

	private static Object append(Object[] current, Object[] value) {
		if (current == null) current = new Object[0];
		System.arraycopy(current, 0, value, 0, current.length);
		return value;
	}

	@SuppressWarnings("unchecked")
	private static List<?> append(List current, Object value) {
		if (current == null) current = new ArrayList<>();
		current.add(value);
		return current;
	}

	@SuppressWarnings("unchecked")
	private static List<?> append(List current, List value) {
		if (current == null) current = new ArrayList<>();
		current.addAll(value);
		return current;
	}

	private static <T> Field fieldByName(Class<T> aClass, String attr) {
		String attrId = className(aClass) + "" + attr.toLowerCase();
		if (!fields.containsKey(attrId)) findField(aClass, attr, attrId);
		return fields.get(attrId);
	}

	private static <T> void findField(Class<T> aClass, String attr, String attrId) {
		for (Field field : Fields.of(aClass))
			if (attr.equalsIgnoreCase(field.getName()) || attr.equalsIgnoreCase(simpleClassName(field))) {
				fields.put(attrId, field);
				break;
			}
	}

	private static String simpleClassName(Field field) {
		final Class<?> aClass = classOf(field);
		String name = className(aClass);
		name = name.contains("$") ? name.substring(name.lastIndexOf("$") + 1) : name;
		name = name.contains(".") ? name.substring(name.lastIndexOf(".") + 1) : name;
		return name;
	}

	private static String className(Class<?> aClass) {
		if (!classNames.containsKey(aClass)) classNames.put(aClass, aClass.getCanonicalName());
		return classNames.get(aClass);
	}

	private static Class<?> classOf(Field field) {
		if (!(field.getGenericType() instanceof ParameterizedType)) return field.getType();
		ParameterizedType type = (ParameterizedType) field.getGenericType();
		return (Class<?>) type.getActualTypeArguments()[0];
	}

	private static Object create(Class<?> type) {
		try {
			return type.getDeclaredConstructors()[0].newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}
}
