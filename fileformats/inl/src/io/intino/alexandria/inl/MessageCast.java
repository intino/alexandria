package io.intino.alexandria.inl;

import io.intino.alexandria.Resource;
import io.intino.alexandria.inl.helpers.Fields;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;


@SuppressWarnings("unchecked")
public class MessageCast {
	private static Map<Class, String> classNames = new HashMap<>();
	private static Map<String, Field> fields = new HashMap<>();
	private Message message;

	private MessageCast(Message message) {
		this.message = message;
	}

	public static MessageCast cast(Message message) {
		return new MessageCast(message);
	}

	private static <T> Object fillObject(Message message, Class<T> aClass, Object object) throws IllegalAccessException {
		fillAttributes(message, aClass, object);
		fillComponents(message, aClass, object);
		return object;
	}

	private static <T> void fillAttributes(Message message, Class<T> aClass, Object object) throws IllegalAccessException {
		for (String attribute : message.attributes()) {
			Field field = fieldByName(aClass, attribute);
			if (field != null) setField(field, object, valueOf(message, attribute, field));
		}
	}

	private static Object valueOf(Message message, String attribute, Field field) {
		if (field.getType().isAssignableFrom(Resource.class)) {
			Message.Attachment attachment = message.attachment(message.get(attribute));
			return new Resource(idOf(attachment)).data(attachment.data());
		}
		return parserOf(field).parse(message.get(attribute));
	}

	private static String idOf(Message.Attachment attachment) {
		String id = attachment.id();
		if (id.contains("#")) id = id.substring(id.indexOf("#") + 1);
		return id;
	}

	private static <T> void fillComponents(Message message, Class<T> aClass, Object object) throws IllegalAccessException {
		for (Message component : message.components()) {
			Field field = fieldByName(aClass, component.type());
			if (field != null)
				setField(field, object, fillObject(component, classOf(field), create(classOf(field))));
		}
	}

	private static void setField(Field field, Object owner, Object value) throws IllegalAccessException {
		field.setAccessible(true);
		if (field.getType().isAssignableFrom(List.class))
			field.set(owner, value instanceof List ? append((List) field.get(owner), (List) value) : append((List) field.get(owner), value));
		else if (field.getType().isArray())
			field.set(owner, append((Object[]) field.get(owner), (Object[]) value));
		else field.set(owner, value);
	}

	private static Object append(Object[] current, Object[] value) {
		if (current == null) current = new Object[0];
		System.arraycopy(current, 0, value, 0, current.length);
		return value;
	}

	private static List append(List current, Object value) {
		if (current == null) current = new ArrayList();
		current.add(value);
		return current;
	}

	private static List append(List current, List value) {
		if (current == null) current = new ArrayList();
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
			if (attr.equalsIgnoreCase(field.getName()) || attr.equalsIgnoreCase(className(field))) {
				fields.put(attrId, field);
				break;
			}
	}

	private static String className(Field field) {
		final Class aClass = classOf(field);
		return className(aClass);
	}

	private static String className(Class aClass) {
		if (!classNames.containsKey(aClass)) classNames.put(aClass, aClass.getSimpleName());
		return classNames.get(aClass);
	}

	private static Class classOf(Field field) {
		if (!(field.getGenericType() instanceof ParameterizedType)) return field.getType();
		ParameterizedType ptype = (ParameterizedType) field.getGenericType();
		return (Class) ptype.getActualTypeArguments()[0];
	}

	private static Object create(Class<?> type) {
		try {
			return type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Parser parserOf(Field field) {
		return isList(field) ? listParserOf(field.getGenericType().toString()) : Parser.of(field.getType());
	}

	private static boolean isList(Field field) {
		return field.getType().isAssignableFrom(List.class);
	}

	private static Parser listParserOf(final String name) {
		return new Parser() {
			Parser parser = Parser.of(arrayClass());

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
				return Arrays.asList(array);
			}
		};
	}

	public <T> T as(Class<T> aClass) throws IllegalAccessException {
		return message != null ? (T) fillObject(message, aClass, create(aClass)) : null;
	}

}
