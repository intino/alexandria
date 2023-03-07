package io.intino.alexandria.message;


import java.lang.reflect.Array;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

class ParserFactory {
	private static final Map<Class<?>, Parser> Parsers;

	static {
		Parsers = new HashMap<>();
		primitives();
		boxed();
		text();
		datetime();
	}

	private static void primitives() {
		register(boolean.class, Boolean::parseBoolean);
		register(byte.class, Byte::parseByte);
		register(char.class, s -> (char)Integer.parseInt(s));
		register(short.class, Short::parseShort);
		register(int.class, Integer::parseInt);
		register(long.class, Long::parseLong);
		register(float.class, Float::parseFloat);
		register(double.class, Double::parseDouble);
	}

	private static void boxed() {
		register(Boolean.class, Parsers.get(boolean.class));
		register(Byte.class, Parsers.get(byte.class));
		register(Character.class, Parsers.get(char.class));
		register(Short.class, Parsers.get(short.class));
		register(Integer.class, Parsers.get(int.class));
		register(Long.class, Parsers.get(long.class));
		register(Float.class, Parsers.get(float.class));
		register(Double.class, Parsers.get(double.class));
	}

	private static void text() {
		register(String.class, text -> text);
		register(CharSequence.class, text -> text);
	}

	private static void datetime() {
		register(Instant.class, Instant::parse);
		register(LocalTime.class, LocalTime::parse);
		register(LocalDate.class, LocalDate::parse);
		register(LocalDateTime.class, LocalDateTime::parse);
	}

	static Parser get(Class<?> class_) {
		return Parsers.get(class_);
	}

	private static void register(Class<?> clazz, Parser parser) {
		Parsers.put(clazz, parser);
		if(!clazz.isArray()) registerArrayOf(clazz);
	}

	private static void registerArrayOf(Class<?> clazz) {
		Parsers.put(Array.newInstance(clazz, 0).getClass(), ArrayParser.of(clazz));
	}

	private static class ArrayParser implements Parser {
		private final Class<?> elementType;

		private ArrayParser(Class<?> elementType) {
			this.elementType = elementType;
		}

		@Override
		public Object parse(String text) {
			Parser parser = Parser.of(elementType);
			String[] items = text.split(Message.ListSepStr);
			Object result = Array.newInstance(elementType, items.length);
			for (int i = 0; i < items.length; i++) {// TODO: trim items?
				String item = items[i];
				Array.set(result, i, (Message.NullValue.equals(item) ? null : parser.parse(item)));
			}
			return result;
		}

		private static ArrayParser of(Class<?> type) {
			return new ArrayParser(type);
		}
	}
}
