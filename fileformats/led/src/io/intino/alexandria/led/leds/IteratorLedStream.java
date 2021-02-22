package io.intino.alexandria.led.leds;

import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.Schema;

import java.util.Iterator;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class IteratorLedStream<T extends Schema> implements LedStream<T> {

    public static <S extends Schema> IteratorLedStream<S> fromStream(Class<S> schemaClass, Stream<S> stream) {
        return new IteratorLedStream<>(schemaClass, stream.iterator());
    }


    private final Class<T> schemaClass;
    private final Iterator<T> iterator;
    private final int schemaSize;
    private Runnable onClose;

    public IteratorLedStream(Class<T> schemaClass, Iterator<T> iterator) {
        this.schemaClass = schemaClass;
        this.iterator = requireNonNull(iterator);
        this.schemaSize = Schema.sizeOf(schemaClass);
    }

    @Override
    public void close() throws Exception {
        if(onClose != null) {
            onClose.run();
            onClose = null;
        }
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public LedStream<T> onClose(Runnable onClose) {
        this.onClose = onClose;
        return this;
    }

    @Override
    public Class<T> schemaClass() {
        return schemaClass;
    }

    @Override
    public int schemaSize() {
        return schemaSize;
    }
}
