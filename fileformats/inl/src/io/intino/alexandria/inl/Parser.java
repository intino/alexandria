package io.intino.alexandria.inl;

interface Parser {
    Object parse(String text);

    static Parser of(Class<?> aClass) { return ParserFactory.get(aClass); }

}
