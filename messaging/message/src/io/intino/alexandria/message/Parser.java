package io.intino.alexandria.message;

interface Parser {
	Object parse(String text);

	static Parser of(Class<?> aClass) {
		return ParserFactory.get(aClass);
	}

}
