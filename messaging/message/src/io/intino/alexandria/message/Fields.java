package io.intino.alexandria.message;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

class Fields {

	static FieldQuery of(Object object) {
		return new FieldQuery(object);
	}

	static List<Field> of(Class type) {
		if (type == null) return new ArrayList<>();
		List<Field> list = of(type.getSuperclass());
		list.addAll(asList(type.getDeclaredFields()));
		return list;
	}

	static class FieldQuery {
		private final Object object;

		FieldQuery(Object object) {
			this.object = object;
		}

		List<Field> asList() {
			return of(object.getClass());
		}

	}

}
