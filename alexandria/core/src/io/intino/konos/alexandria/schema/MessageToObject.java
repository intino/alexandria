package io.intino.konos.alexandria.schema;

import io.intino.ness.inl.Message;
import io.intino.ness.inl.Message.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.intino.konos.alexandria.schema.Deserializer.create;
import static io.intino.konos.alexandria.schema.Deserializer.parserOf;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

@SuppressWarnings("unchecked")
public class MessageToObject {
	private static Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);
	private static Map<Class, String> classNames = new HashMap<>();

	public static <T> T fromMessage(Message message, Class<T> aClass) {
		return (T) fillObject(message, aClass, create(aClass));
	}

	private static <T> Object fillObject(Message message, Class<T> aClass, Object object) {
		attributes(message, aClass, object);
		components(message, aClass, object);
		return object;
	}

	private static <T> void attributes(Message message, Class<T> aClass, Object object) {
		for (String attr : message.attributes()) {
			Field field = fieldByName(aClass, attr);
			if (field != null) setField(field, object, valueOf(message, attr, field));
		}
	}

	private static Object valueOf(Message message, String attr, Field field) {
		if (field.getType().isAssignableFrom(Resource.class)) {
			Attachment attachment = message.attachment(message.get(attr));
			return new Resource(attachment.id()).data(attachment.data());
		}
		return parserOf(field).parse(message.get(attr));
	}

	private static <T> void components(Message message, Class<T> aClass, Object object) {
		for (Message component : message.components()) {
			Field field = fieldByName(aClass, component.type());
			if (field != null) setField(field, object, fillObject(component, classOf(field), create(classOf(field))));
		}
	}

	private static void setField(Field field, Object owner, Object value) {
		try {
			field.setAccessible(true);
			if (field.getType().isAssignableFrom(List.class))
				field.set(owner, value instanceof List ? append((List) field.get(owner), (List) value) : append((List) field.get(owner), value));
			else if (field.getType().isArray()) field.set(owner, append((Object[]) field.get(owner), (Object[]) value));
			else field.set(owner, value);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		}
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
		return Accessory.fieldsOf(aClass).stream().filter(f -> match(f, attr)).findFirst().orElse(null);
	}

	private static boolean match(Field field, String attribute) {
		return attribute.equalsIgnoreCase(field.getName()) ||
				attribute.equalsIgnoreCase(className(field));
	}

	private static String className(Field field) {
		final Class aClass = classOf(field);
		if (!classNames.containsKey(aClass)) classNames.put(aClass, aClass.getSimpleName());
		return classNames.get(aClass);
	}

	private static Class classOf(Field field) {
		if (!(field.getGenericType() instanceof ParameterizedType)) return field.getType();
		ParameterizedType ptype = (ParameterizedType) field.getGenericType();
		return (Class) ptype.getActualTypeArguments()[0];
	}


}
