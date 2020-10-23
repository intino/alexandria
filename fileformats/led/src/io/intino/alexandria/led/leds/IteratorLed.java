package io.intino.alexandria.led.leds;

import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.Transaction;

import java.util.Iterator;
import java.util.stream.Stream;

public class IteratorLed<T extends Transaction> implements LedStream<T> {

    private final Iterator<T> iterator;
    private final int transactionSize;

    public IteratorLed(int transactionSize, Iterator<T> iterator) {
        this.iterator = iterator;
        this.transactionSize = transactionSize;
    }

    public IteratorLed(int transactionSize, Stream<T> stream) {
        this(transactionSize, stream.iterator());
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
