package io.intino.alexandria.inl;

import io.intino.alexandria.Resource;
import io.intino.alexandria.inl.helpers.Fields;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static io.intino.alexandria.inl.InlBuilder.isAttachment;
import static io.intino.alexandria.inl.InlBuilder.isAttribute;
import static io.intino.alexandria.inl.InlBuilder.isEmpty;
import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isTransient;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

public class MessageBuilder {

    public static Message build(Object object) {
        return new MessageBuilder().getMessage(object);
    }

    private Message getMessage(Object object) {
        final Message message = new Message(object.getClass().getSimpleName());
        for (Field field : Fields.of(object).asList()) {
            if (!isConvertible(field)) continue;
            Object value = valueOf(field, object);
            if (isNull(value) || isEmpty(value)) continue;
            if (isAttachment(field)) convertAttachment(message, field, value);
            else if (isAttribute(field)) convertAttribute(message, field, value);
            else {
                if (isList(field) || isArray(field)) for (Object v : valuesOf(field, object)) message.add(build(v));
                else message.add(build(value));
            }
        }
        return message;
    }

    private static void convertAttachment(Message message, Field field, Object value) {
        Resource resource = (Resource) value;
        message.attach(field.getName(), resource.id(), resource.type(), resource.data());
        reset(resource);
    }

    private static void reset(Resource resource) {
        try {
            resource.data().reset();
        } catch (IOException ignored) {
        }
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
        if (value instanceof Double) message.append(name, (Double) value);
        else if (value instanceof Boolean) message.append(name, (Boolean) value);
        else if (value instanceof Integer) message.append(name, (Integer) value);
        else message.append(name, value == null ? null : value.toString());
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
}
