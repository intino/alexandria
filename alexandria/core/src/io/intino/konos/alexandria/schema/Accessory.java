package io.intino.konos.alexandria.schema;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class Accessory {

	public static FieldQuery fieldsOf(Object object) {
		return new FieldQuery(object);
	}

	static List<Field> fieldsOf(Class type) {
		if (type == null) return new ArrayList<>();
		List<Field> list = fieldsOf(type.getSuperclass());
		list.addAll(asList(type.getDeclaredFields()));
		return list;
	}

	static Object valueOf(Field field, Object owner) {
		try {
			field.setAccessible(true);
			return field.get(owner);
		} catch (IllegalAccessException e) {
			return null;
		}
	}

	public static class Mapping {

		private Map<String, String> map = new HashMap<>();

		public void put(String from, String to) {
			map.put(from.toLowerCase(), to);
		}

		public String get(String text) {
			return map.getOrDefault(text.toLowerCase(), text);
		}
	}

	public static class FieldQuery {
		private final Object object;

		FieldQuery(Object object) {
			this.object = object;
		}

		public List<Field> asList() {
			return fieldsOf(object.getClass());
		}

		Field get(String name) {
			for (Field field : fieldsOf(object.getClass()))
				if (name.equalsIgnoreCase(field.getName())) return field;
			throw new RuntimeException(name + " attribute doesn't exist");
		}
	}

}
