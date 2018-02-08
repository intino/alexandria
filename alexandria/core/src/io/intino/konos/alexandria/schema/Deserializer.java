package io.intino.konos.alexandria.schema;


import io.intino.konos.alexandria.schema.Accessory.Mapping;
import io.intino.konos.alexandria.schema.Parsers.Parser;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.intino.konos.alexandria.schema.Accessory.fieldsOf;


public class Deserializer {
    private final BufferedReader reader;
	private final ResourceLoader[] loaders;
	private final Mapping mapping = new Mapping();
    private String line;

	public static Deserializer deserialize(String text, ResourceLoader... loaders) {
		return deserialize(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)), loaders);
	}

    public static Deserializer deserialize(InputStream is, ResourceLoader... loaders) {
        return new Deserializer(is, loaders);
    }

    private Deserializer(InputStream is, ResourceLoader[] loaders) {
        this.reader = new BufferedReader(new InputStreamReader(is));
		this.loaders = loaders;
		nextLine();
    }

    @SuppressWarnings("unchecked")
    public <T> T next(Class<T> type) {
        if (!startBlockOf(type)) return null;
        return fill((T) create(type));
    }

    private boolean startBlockOf(Class type) {
        return line != null && map(unwrapBlock(line)).equalsIgnoreCase(type.getSimpleName());
    }

	private String unwrapBlock(String text) {
		return text.startsWith("[") ? text.substring(1, text.length() - 1) : text;
	}

	private String unwrap(String value) {
		return value.startsWith("\"") && value.endsWith("\"") ? value.substring(1, value.length() - 1) : value;
	}

    private <T> T fill(T object) {
        Object scope = object;
        String attribute = "";
        String value = "";
        nextLine();
        while (!isTerminated(object)) {
            if (isMultiline()) setAttribute(scope, attribute, value = (value != null ? value + "\n" : "") + line.substring(1));
            else if (isHeader()) scope = addComponent(object, line.substring(1,line.length()-1));
            else if (isAttribute()) setAttribute(scope, attribute = attributeOf(line), value = valueOf(line));
            nextLine();
        }
        return object;
    }

	private String attributeOf(String line) {
		return line.substring(0, line.indexOf(":"));
	}

	private String valueOf(String line) {
		return line.indexOf(":") + 1 < line.length() ? unwrap(line.substring(line.indexOf(":") + 1)) : null;
	}

	private boolean isMultiline() {
		return line.startsWith("\t");
	}

	private String map(String id) {
		return mapping.get(id);
	}

	private boolean isHeader() {
		return line.startsWith("[");
	}

	private boolean isAttribute() {
		return line.contains(":");
	}

	private boolean isTerminated(Object object) {
        return line == null || startBlockOf(object.getClass());
    }

	private void setAttribute(Object object, String attribute, String value) {
		if (object == null || value == null || value.isEmpty()) return;
		Field field = fieldsOf(object).get(map(attribute));
		setField(field, object, parserOf(field).parse(deIndent(value)));
	}

    private Object addComponent(Object scope, String path) {
        String[] paths = path.split("\\.");
        for (int i = 1; i < paths.length-1; i++) {
            scope = findScope(scope, paths[i]);
            if (scope == null) return null;
        }
        return createComponent(paths[paths.length-1], scope);
    }

	private Object valueOf(Field field, Object object) {
		try {
			field.setAccessible(true);
			return field.get(object);
		}
		catch (IllegalAccessException e) {
			return null;
		}
	}

	private String deIndent(String value) {
        return value.startsWith("\n") ? value.substring(1) : value;
    }

    private Object findScope(Object object, String attribute) {
        for (Field field : fieldsOf(object).asList()) {
            if (!match(field, attribute)) continue;
            Object result = valueOf(field, object);
            return result instanceof List ? lastItemOf((List) result) : result;
        }
        return null;
    }

    private boolean match(Field field, String attribute) {
        return  attribute.equalsIgnoreCase(field.getName()) ||
                attribute.equalsIgnoreCase(classOf(field).getSimpleName());
    }

    private Object lastItemOf(List list) {
        return list.get(list.size()-1);
    }

    private Class classOf(Field field) {
        if (!(field.getGenericType() instanceof ParameterizedType)) return field.getType();
        ParameterizedType ptype = (ParameterizedType) field.getGenericType();
        return (Class) ptype.getActualTypeArguments()[0];
    }

    private Object createComponent(String type, Object object) {
        return createComponent(findField(type, object), object);
    }

    private Object createComponent(Field field, Object object) {
        if (field == null) return null;
		return isList(field) ? createListItem(field, object) : setField(field, object, create(classOf(field)));
	}

    @SuppressWarnings("unchecked")
    private Object createListItem(Field field, Object object) {
        List list = (List) valueOf(field, object);
        if (list == null) {
            list = new ArrayList<>();
            setField(field, object, list);
        }
        Object item = create(classOf(field));
        list.add(item);
        return item;
    }

    private boolean isList(Field field) {
        return field.getType().isAssignableFrom(List.class);
    }

    private Object setField(Field field, Object object, Object value) {
        if (field == null) return null;
        try {
			field.setAccessible(true);
			if (value.getClass().isArray()) value = append((Object[]) field.get(object), (Object[]) value);
			if (value instanceof List) value = append((List) field.get(object), (List) value);
			if (value instanceof Resource) load((Resource) value);
			field.set(object, value);
			return value;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

	private Object append(Object[] current, Object[] value) {
		if (current == null) current = new Object[0];
		System.arraycopy(current, 0, value, 0, current.length);
		Object o = value[current.length];
		if (o instanceof Resource) load((Resource) o);
		return value;
	}

	private List append(List current, List value) {
		if (current == null) current = new ArrayList();
		final Object o = value.get(value.size() - 1);
		if (o instanceof Resource) load((Resource) o);
		//noinspection unchecked
		current.add(o);
		return current;
	}

	private void load(Resource resource) {
		for (ResourceLoader loader : loaders) {
			final byte[] data = loader.load(resource.id());
			if (data == null) continue;
			resource.data(data);
			return;
		}
	}

	private Field findField(String type, Object object) {
        for (Field field : fieldsOf(object).asList()) {
            if (!match(field, type)) continue;
            if (isList(field) || valueOf(field, object) == null) return field;
        }
        return null;
    }

    private Object create(Class<?> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    private void nextLine()  {
        try {
            do {
                line = normalize(reader.readLine());
            } while (line != null && line.isEmpty());
        }
        catch (IOException e) {
            line = null;
        }
    }

	private Parser parserOf(Field field) {
		return isList(field) ? listParserOf(field.getGenericType().toString()) : Parsers.get(field.getType());
	}

	private Parser listParserOf(final String name) {
		return new Parser() {
			Parser parser = Parsers.get(arrayClass());

			private Class<?> arrayClass() {
				try {
					String className = "[L" + name.substring(name.indexOf('<')+1).replace(">","") +  ";";
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

	public Deserializer map(String from, String to) {
        mapping.put(from,to);
        return this;
    }

	private static String normalize(String line) {
		if (line == null) return null;
		if (line.startsWith("\t")) return line;
		line = line.trim();
		if (line.isEmpty()) return line;
		if (line.startsWith("[")) return line;
		return line.replaceAll("(\\w*)\\s*[:=]\\s*(.*)", "$1:$2");
	}


}
