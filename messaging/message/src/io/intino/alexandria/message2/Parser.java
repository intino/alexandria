package io.intino.alexandria.message2;

interface Parser {
    Object parse(String text);

    static Parser of(Class<?> aClass) {
        return ParserFactory.get(aClass);
    }

}
