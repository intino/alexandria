package io.intino.alexandria.inl;

import io.intino.alexandria.Resource;
import io.intino.alexandria.inl.helpers.Fields;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static io.intino.alexandria.inl.InlDeserializer.create;
import static io.intino.alexandria.inl.InlDeserializer.parserOf;
import static io.intino.alexandria.inl.InlSerializer.*;
import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isTransient;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

public class Inl {
	public static Message toMessage(Object object) throws IOException {
		return ObjectToMessage.toMessage(object);
	}

	public static <T> T fromMessage(Message object, Class<T> t) throws IllegalAccessException {
		return MessageToObject.fromMessage(object, t);
	}

	public static class ObjectToMessage {


		public static Message toMessage(Object object) throws IOException {
			final Message message = new Message(object instanceof List ? collectionType((List) object) : object.getClass().getSimpleName());
			for (Field field : Fields.of(object).asList()) {
				if (!isConvertible(field)) continue;
				Object value = valueOf(field, object);
				if (isNull(value) || isEmpty(value)) continue;
				if (isAttachment(field)) convertAttachment(message, field, value);
				else if (isAttribute(field)) convertAttribute(message, field, value);
				else {
					if (isList(field) || isArray(field)) {
						for (Object v : valuesOf(field, object)) message.add(toMessage(v));
					}
					else message.add(toMessage(value));
				}
			}
			return message;
		}

		private static void convertAttachment(Message message, Field field, Object value) throws IOException {
			Resource resource = (Resource) value;
			message.set(field.getName(), resource.id(), resource.type(), resource.data());
			resource.data().reset();
		}

		@SuppressWarnings("unchecked")
		private static void convertAttribute(Message message, Field field, Object value) {
			if (isList(field)) ((List) value).forEach(o -> writeAttribute(message, field, o));
			else if (isArray(field)) Arrays.asList((Object[]) value).forEach(ob -> writeAttribute(message, field, ob));
			else writeAttribute(message, field, value);
		}

		private static boolean isList(Field field) {
			return field.getType().isAssignableFrom(List.class);
		}

		private static boolean isArray(Field field) {
			return field.getType().isArray();
		}

		private static void writeAttribute(Message message, Field field, Object value) {
			final String name = field.getName();
			if (value instanceof Double) message.write(name, (Double) value);
			else if (value instanceof Boolean) message.write(name, (Boolean) value);
			else if (value instanceof Integer) message.write(name, (Integer) value);
			else message.write(name, value == null ? null : value.toString());
		}

		private static boolean isConvertible(Field field) {
			return !(isTransient(field.getModifiers()) || isStatic(field.getModifiers()));
		}

		private static Object valueOf(Field field, Object object) {
			try {
				field.setAccessible(true);
				return field.get(object);
			} catch (IllegalAccessException e) {
				return null;
			}
		}

		@SuppressWarnings("unchecked")
		private static List<Object> valuesOf(Field field, Object object) {
			final Object o = valueOf(field, object);
			return o == null ? emptyList() : o instanceof List ? (List<Object>) o : Arrays.asList((Object[]) o);
		}

		private static String collectionType(List object) {
			final String typeName = ((ParameterizedType) (object).getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
			return typeName.contains("") ? typeName.substring(typeName.lastIndexOf("") + 1) : typeName;
		}

	}

	@SuppressWarnings("unchecked")
	public static class MessageToObject {
		private static Map<Class, String> classNames = new HashMap<>();
		private static Map<String, Field> fields = new HashMap<>();

		public static <T> T fromMessage(Message message, Class<T> aClass) throws IllegalAccessException {
			return (T) fillObject(message, aClass, create(aClass));
		}

		private static <T> Object fillObject(Message message, Class<T> aClass, Object object) throws IllegalAccessException {
			attributes(message, aClass, object);
			components(message, aClass, object);
			return object;
		}

		private static <T> void attributes(Message message, Class<T> aClass, Object object) throws IllegalAccessException {
			for (String attr : message.attributes()) {
				Field field = fieldByName(aClass, attr);
				if (field != null) setField(field, object, valueOf(message, attr, field));
			}
		}

		private static Object valueOf(Message message, String attr, Field field) {
			if (field.getType().isAssignableFrom(Resource.class)) {
				Message.Attachment attachment = message.attachment(message.get(attr));
				return new Resource(attachment.id()).data(attachment.data());
			}
			return parserOf(field).parse(message.get(attr));
		}

		private static <T> void components(Message message, Class<T> aClass, Object object) throws IllegalAccessException {
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


	}
}
