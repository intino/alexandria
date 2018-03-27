package io.intino.konos.alexandria.schema;

import io.intino.ness.inl.Message;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static io.intino.konos.alexandria.schema.Serializer.isAttribute;
import static io.intino.konos.alexandria.schema.Serializer.isEmpty;
import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isTransient;
import static java.util.Objects.isNull;

public class ObjectToMessage {


	public static Message toMessage(Object object) {
		final Message message = new Message(object.getClass().getSimpleName());
		for (Field field : Accessory.fieldsOf(object).asList()) {
			if (!isConvertible(field)) continue;
			Object value = valueOf(field, object);
			if (isNull(value) || isEmpty(value)) continue;
			if (isAttribute(field)) convertAttribute(message, field, value);
			else message.add(toMessage(value));
		}
		return message;
	}

	private static void convertAttribute(Message message, Field field, Object value) {
		if (isList(field)) ((List) value).forEach(o -> writeAttribute(message, field, o));
		else if (isArray(field)) Arrays.asList((Object[]) value).forEach(o -> writeAttribute(message, field, o));
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

}
