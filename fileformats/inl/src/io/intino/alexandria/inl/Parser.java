package io.intino.alexandria.inl;

interface Parser {
    String NullValue = "\0";
    Object parse(String text);

    static Parser of(Class<?> aClass) { return ParserFactory.get(aClass); }

}
