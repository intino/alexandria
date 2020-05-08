package io.intino.alexandria.message2;

interface Formatter {
    String format(Object value);
    static Formatter of(Class<?> aClass) { return FormatterFactory.get(aClass); }

}
