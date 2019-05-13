package io.intino.alexandria.inl;

interface Formatter {
    String format(Object value);
    static Formatter of(Class<?> aClass) { return FormatterFactory.get(aClass); }

}
