package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.allocators.stack.StackAllocator;
import io.intino.alexandria.led.allocators.stack.StackAllocators;
import io.intino.alexandria.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.intino.alexandria.led.Transaction.factoryOf;
import static io.intino.alexandria.led.Transaction.sizeOf;
import static io.intino.alexandria.led.util.MemoryUtils.allocBuffer;

public class InputLedStream<T extends Transaction> implements LedStream<T> {

    private static final int DEFAULT_BUFFER_SIZE = 4096;

    private final InputStream inputStream;
    private final int bufferSize;
    private final int transactionSize;
    private final TransactionFactory<T> provider;
    private final Iterator<T> iterator;
    private Runnable onClose;
    private boolean closed;

    public InputLedStream(InputStream inputStream, Class<T> transactionClass) {
        this(inputStream, factoryOf(transactionClass), sizeOf(transactionClass), DEFAULT_BUFFER_SIZE);
    }

    public InputLedStream(InputStream inputStream, Class<T> transactionClass, int bufferSize) {
        this(inputStream, factoryOf(transactionClass), sizeOf(transactionClass), bufferSize);
    }

    public InputLedStream(InputStream inputStream, TransactionFactory<T> factory, int transactionSize) {
        this(inputStream, factory, transactionSize, DEFAULT_BUFFER_SIZE);
    }

    public InputLedStream(InputStream inputStream, TransactionFactory<T> factory, int transactionSize, int bufferSize) {
        this.inputStream = inputStream;
        this.transactionSize = transactionSize;
        this.provider = factory;
        this.iterator = stream().iterator();
        this.bufferSize = bufferSize;
    }

    public int bufferSize() {
        return bufferSize;
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

    @SuppressWarnings("unchecked")
    public synchronized Stream<T> stream() {
        return Stream.generate(() -> read(inputStream))
                .takeWhile(inputBuffer -> checkInputBuffer(inputBuffer, inputStream))
                .flatMap(this::allocateAll);
    }

    @Override
    public int transactionSize() {
        return transactionSize;
    }

    private boolean checkInputBuffer(ByteBuffer inputBuffer, InputStream inputStream) {
        if (inputBuffer != null) return true;
        closeInputStream(inputStream);
        return false;
    }

    private synchronized void closeInputStream(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    private Stream<T> allocateAll(ByteBuffer bytes) {
        StackAllocator<T> allocator = StackAllocators.newManaged(transactionSize, bytes, provider);
        return IntStream.range(0, bytes.remaining() / transactionSize)
                .sorted()
                .parallel()
                .mapToObj(index -> allocator.malloc());
    }

    private synchronized ByteBuffer read(InputStream inputStream) {
        try {
            if (inputStream == null || inputStream.available() <= 0) return null;
            byte[] inputBuffer = new byte[bufferSize * transactionSize];
            int bytesRead;
            bytesRead = inputStream.read(inputBuffer);
            if (bytesRead < 0) return null;
            ByteBuffer buffer = allocBuffer(bytesRead);
            buffer.put(inputBuffer, 0, bytesRead);
            buffer.clear();
            return buffer;
        } catch (Exception e) {
            Logger.error(e);
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        if (closed) {
            return;
        }
        if (onClose != null) {
            onClose.run();
        }
        inputStream.close();
        closed = true;
    }
}
