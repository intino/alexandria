package io.intino.alexandria.message2;

import io.intino.alexandria.Resource;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

class FormatterFactory {
	private static final String NullValue = "\0";
	private static final Map<Class, Formatter> formatters = new HashMap<>();

	public static Formatter get(Class<?> aClass) {
		return formatters.get(aClass);
	}

	public static void put(Class<?> aClass, Formatter formatter) {
		formatters.put(aClass, formatter);
	}

	static {
		formatters.put(String.class, FormatterFactory::formatText);
		formatters.put(Boolean.class, Object::toString);
		formatters.put(Byte.class, Object::toString);
		formatters.put(Integer.class, Object::toString);
		formatters.put(Float.class, Object::toString);
		formatters.put(Double.class, Object::toString);
		formatters.put(Instant.class, Object::toString);
		formatters.put(Resource.class, FormatterFactory::formatResource);
		formatters.put(String[].class, ArrayFormatter.of(String.class)::format);
		formatters.put(Boolean[].class, ArrayFormatter.of(Boolean.class)::format);
		formatters.put(Byte[].class, ArrayFormatter.of(Byte.class)::format);
		formatters.put(Integer[].class, ArrayFormatter.of(Integer.class)::format);
		formatters.put(Float[].class, ArrayFormatter.of(Float.class)::format);
		formatters.put(Double[].class, ArrayFormatter.of(Double.class)::format);
		formatters.put(Instant[].class, ArrayFormatter.of(Instant.class)::format);
		formatters.put(Resource[].class, ArrayFormatter.of(Resource.class)::format);
	}

	private static String formatText(Object o) {
		if (o == null) return NullValue;
		String text = o.toString();
		return text.contains("\n") ? "\n\t" + text.replaceAll("\n", "\n\t") : text;
	}

	private static String formatResource(Object o) {
		if (o == null) return NullValue;
		return "@" + o.toString();
	}


	static class ArrayFormatter {
		private Formatter formatter;

		ArrayFormatter(Formatter formatter) {
			this.formatter = formatter;
		}

		static ArrayFormatter of(Class type) {
			return new ArrayFormatter(formatters.get(type));
		}

		String format(Object o) {
			StringBuilder result = new StringBuilder();
			for (Object item : (Object[]) o)
				result.append("\n\t").append(item == null ? NullValue : formatter.format(item));
			return result.toString();
		}
	}
}
