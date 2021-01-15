package io.intino.alexandria.led.leds;

import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.Schema;

import java.util.Iterator;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class IteratorLedStream<T extends Schema> implements LedStream<T> {

    public static <S extends Schema> IteratorLedStream<S> fromStream(int schemaSize, Stream<S> stream) {
        return new IteratorLedStream<>(schemaSize, stream.iterator());
    }


    private final Iterator<T> iterator;
    private final int schemaSize;
    private Runnable onClose;

    public IteratorLedStream(int schemaSize, Iterator<T> iterator) {
        this.iterator = requireNonNull(iterator);
        this.schemaSize = schemaSize;
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
    public int schemaSize() {
        return schemaSize;
    }
}
