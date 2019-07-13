package io.intino.alexandria.message;

interface Formatter {
    String format(Object value);
    static Formatter of(Class<?> aClass) { return FormatterFactory.get(aClass); }

}
