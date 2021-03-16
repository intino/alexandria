package io.intino.alexandria.led.leds;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.Schema;

public class ArrayLed<T extends Schema> implements Led<T> {

    private final Class<T> schemaClass;
    private final T[] schemas;
    private final int schemaSize;
    private final int length;

    public ArrayLed(Class<T> schemaClass, T[] schemas, int length) {
        this.schemaClass = schemaClass;
        this.schemas = schemas;
        this.schemaSize = Schema.sizeOf(schemaClass);
        this.length = length;
    }

    public ArrayLed(Class<T> schemaClass, T[] schemas) {
        this(schemaClass, schemas, schemas.length);
    }

    @Override
    public long size() {
        return length;
    }

    @Override
    public int schemaSize() {
        return schemaSize;
    }

    @Override
    public T schema(int index) {
        return schemas[index];
    }

    @Override
    public Class<T> schemaClass() {
        return schemaClass;
    }

    public T[] array() {
        return schemas;
    }
}
