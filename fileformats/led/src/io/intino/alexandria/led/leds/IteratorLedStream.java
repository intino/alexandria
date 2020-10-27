package io.intino.alexandria.led.leds;

import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.Transaction;

import java.util.Iterator;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class IteratorLedStream<T extends Transaction> implements LedStream<T> {

    public static <S extends Transaction> IteratorLedStream<S> fromStream(int transactionSize, Stream<S> stream) {
        return new IteratorLedStream<>(transactionSize, stream.sorted().iterator());
    }


    private final Iterator<T> iterator;
    private final int transactionSize;

    public IteratorLedStream(int transactionSize, Iterator<T> iterator) {
        this.iterator = requireNonNull(iterator);
        this.transactionSize = transactionSize;
    }

    @Override
    public void close() throws Exception {

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
    public int transactionSize() {
        return transactionSize;
    }
}
