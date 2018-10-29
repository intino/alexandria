package io.intino.alexandria.inl;

import java.lang.reflect.Array;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static java.lang.reflect.Array.set;

public class Parsers {

	private static final String NullValue = "\0";
	private static final Map<Class, Parser> parsers = new HashMap<>();

	public static Parser get(Class<?> aClass) {
		return parsers.get(aClass);
	}

	public static void put(Class<?> aClass, Parser parser) {
		parsers.put(aClass, parser);
	}

	public interface Parser {
		Object parse(String text);
	}

	private static class ArrayParser {
		private Class type;
		private Parser parser;

		ArrayParser(Class type, Parser parser) {
			this.type = type;
			this.parser = parser;
		}

		static ArrayParser of(Class type) {
			return new ArrayParser(type, parsers.get(type));
		}

		Object parse(String text) {
			String[] lines = text.split("\n");
			Object result = Array.newInstance(type, lines.length);
			for (int i = 0; i < lines.length; i++) {
				set(result, i, (NullValue.equals(lines[i]) ? null : parser.parse(lines[i])));
			}
			return result;
		}
	}

	static {
		parsers.put(boolean.class, Boolean::parseBoolean);
		parsers.put(byte.class, Byte::parseByte);
		parsers.put(int.class, Integer::parseInt);
		parsers.put(float.class, Float::parseFloat);
		parsers.put(double.class, Double::parseDouble);
		parsers.put(long.class, Long::parseLong);
		parsers.put(Boolean.class, parsers.get(boolean.class));
		parsers.put(Byte.class, parsers.get(byte.class));
		parsers.put(Integer.class, parsers.get(int.class));
		parsers.put(Long.class, parsers.get(long.class));
		parsers.put(Float.class, parsers.get(float.class));
		parsers.put(Double.class, parsers.get(double.class));
		parsers.put(String.class, text -> text);
		parsers.put(Instant.class, Instant::parse);
		parsers.put(Resource.class, Resource::parse);
		parsers.put(Boolean[].class, ArrayParser.of(Boolean.class)::parse);
		parsers.put(Byte[].class, ArrayParser.of(Byte.class)::parse);
		parsers.put(Integer[].class, ArrayParser.of(Integer.class)::parse);
		parsers.put(Float[].class, ArrayParser.of(Float.class)::parse);
		parsers.put(Long[].class, ArrayParser.of(Long.class)::parse);
		parsers.put(Double[].class, ArrayParser.of(Double.class)::parse);
		parsers.put(String[].class, ArrayParser.of(String.class)::parse);
		parsers.put(Instant[].class, ArrayParser.of(Instant.class)::parse);
		parsers.put(Resource[].class, ArrayParser.of(Resource.class)::parse);
	}
}
