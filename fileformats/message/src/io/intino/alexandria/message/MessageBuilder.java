package io.intino.alexandria.message;

import io.intino.alexandria.Resource;

import java.lang.reflect.Field;
import java.util.List;

import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isTransient;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;

public class MessageBuilder {
	private static String[] primitives = {"java.lang", Resource.class.getName(), "java.time"};

	public static Message toMessage(Object object) {
		return new MessageBuilder().build(object);
	}

	private Message build(Object object) {
		final Message message = new Message(object.getClass().getSimpleName());
		for (Field field : Fields.of(object).asList())
			if (canBuild(field)) build(message, field, valueOf(field, object));
		return message;
	}

    private void build(Message message, Field field, Object value) {
        if (isNull(value) || isEmpty(value)) return;
        builderOf(field).build(message, field, value);
    }

    private AttributeBuilder builderOf(Field field) {
        return isAttachment(field) ? this::buildAttachment :
               isPrimitive(field) ? this::buildPrimitive : this::buildComposite;
    }

    @SuppressWarnings("unchecked")
    private void buildPrimitive(Message message, Field field, Object value) {
	    valuesIn(field, value).forEach(o -> writeAttribute(message, field, o));
    }

    @SuppressWarnings("unchecked")
    private void buildComposite(Message message, Field field, Object value) {
        valuesIn(field, value).forEach(o -> message.add(toMessage(o)));
    }

    private void buildAttachment(Message message, Field field, Object object) {
		message.set(field.getName(), resource(object).name(), resource(object).bytes());
    }

    private static Resource resource(Object object) {
        return (Resource) object;
    }

    private static List valuesIn(Field field, Object value) {
        return isList(field) ? (List) value :
               isArray(field) ? asList((Object[]) value) : singletonList(value);
    }

	private static boolean isList(Field field) {
		return field.getType().isAssignableFrom(List.class);
	}

	private static boolean isArray(Field field) {
		return field.getType().isArray();
	}

	private static boolean isPrimitive(Field field) {
		Class<?> aClass = field.getType();
		return isPrimitive(aClass) || isEnum(aClass) || isArrayOfPrimitives(aClass) || isListOfPrimitives(field);
	}

	private static boolean isAttachment(Field field) {
        return field.getType().isAssignableFrom(Resource.class);
	}

	private static void writeAttribute(Message message, Field field, Object value) {
		String name = field.getName();
		if (value instanceof Double) message.append(name, (Double) value);
		else if (value instanceof Boolean) message.append(name, (Boolean) value);
		else if (value instanceof Integer) message.append(name, (Integer) value);
		else message.append(name, value == null ? null : value.toString());
	}

	private static boolean canBuild(Field field) {
		return !isTransient(field.getModifiers()) && !isStatic(field.getModifiers());
	}

	private static Object valueOf(Field field, Object object) {
		try {
			field.setAccessible(true);
			return field.get(object);
		} catch (IllegalAccessException e) {
			return null;
		}
	}

	private static boolean isArrayOfPrimitives(Class<?> aClass) {
		return aClass.isArray() && isPrimitive(aClass.getComponentType());
	}

	private static boolean isListOfPrimitives(Field field) {
		return field.getType().isAssignableFrom(List.class) && isPrimitive(field.getGenericType().toString());
	}

	private static boolean isEnum(Class<?> aClass) {
		return aClass.isEnum();
	}

	private static boolean isPrimitive(Class<?> aClass) {
		return isPrimitive(aClass.getName()) || aClass.isPrimitive();
	}

	private static boolean isPrimitive(String className) {
        return checkPrimitives(className.contains("<") ? className.substring(className.indexOf('<') + 1) : className);
	}

    private static boolean checkPrimitives(String name) {
        return stream(primitives).anyMatch(name::startsWith);
    }

    private static boolean isEmpty(Object value) {
		return value == null ||
               value.getClass().isArray() && ((Object[]) value).length == 0 ||
               value instanceof List && ((List) value).isEmpty();
	}

    private interface AttributeBuilder {
        void build(Message message, Field field, Object value);
    }

}
