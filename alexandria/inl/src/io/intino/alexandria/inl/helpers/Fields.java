package io.intino.alexandria.inl.helpers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class Fields {

	public static FieldQuery of(Object object) {
		return new FieldQuery(object);
	}

	public static List<Field> of(Class type) {
		if (type == null) return new ArrayList<>();
		List<Field> list = of(type.getSuperclass());
		list.addAll(asList(type.getDeclaredFields()));
		return list;
	}

	public static Object valueOf(Field field, Object owner) {
		try {
			field.setAccessible(true);
			return field.get(owner);
		} catch (IllegalAccessException e) {
			return null;
		}
	}

	public static class FieldQuery {
		private final Object object;

		FieldQuery(Object object) {
			this.object = object;
		}

		public List<Field> asList() {
			return of(object.getClass());
		}

		public Field get(String name) {
			for (Field field : of(object.getClass()))
				if (name.equalsIgnoreCase(field.getName())) return field;
			throw new RuntimeException(name + " attribute doesn't exist");
		}
	}

}
