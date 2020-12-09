package io.intino.alexandria.led.leds;

import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.allocators.stack.StackAllocator;
import io.intino.alexandria.led.allocators.stack.StackAllocators;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.intino.alexandria.led.Transaction.factoryOf;
import static io.intino.alexandria.led.Transaction.sizeOf;
import static io.intino.alexandria.led.util.memory.LedLibraryConfig.DEFAULT_BUFFER_SIZE;
import static io.intino.alexandria.led.util.memory.LedLibraryConfig.INPUTLEDSTREAM_CONCURRENCY_ENABLED;
import static io.intino.alexandria.led.util.memory.MemoryUtils.allocBuffer;
import static java.nio.file.StandardOpenOption.READ;

public class ByteChannelLedStream<T extends Transaction> implements LedStream<T> {

    private final FileChannel byteChannel;
    private final long fileSize;
    private final int bufferSize;
    private final int transactionSize;
    private final TransactionFactory<T> provider;
    private final Iterator<T> iterator;
    private Runnable onClose;
    private final AtomicBoolean closed;

    public ByteChannelLedStream(File file, Class<T> transactionClass) {
        this(file, factoryOf(transactionClass), sizeOf(transactionClass), DEFAULT_BUFFER_SIZE.get());
    }

    public ByteChannelLedStream(File file, Class<T> transactionClass, int bufferSize) {
        this(file, factoryOf(transactionClass), sizeOf(transactionClass), bufferSize);
    }

    public ByteChannelLedStream(File file, TransactionFactory<T> factory, int transactionSize) {
        this(file, factory, transactionSize, DEFAULT_BUFFER_SIZE.get());
    }

    public ByteChannelLedStream(File file, TransactionFactory<T> factory, int transactionSize, int bufferSize) {
        this.byteChannel = open(file);
        fileSize = getFileSize();
        this.transactionSize = transactionSize;
        this.provider = factory;
        this.bufferSize = bufferSize;
        closed = new AtomicBoolean();
        this.iterator = stream().iterator();
    }

    private long getFileSize() {
        try {
            return byteChannel.size();
        } catch (IOException e) {
            Logger.error(e);
            throw new RuntimeException(e);
        }
    }

    private FileChannel open(File file) {
        try {
            FileChannel byteChannel = FileChannel.open(file.toPath(), READ);
            byteChannel.position(0);
            return byteChannel;
        } catch (IOException e) {
            Logger.error(e);
            throw new RuntimeException(e);
        }
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

    public synchronized Stream<T> stream() {
        return Stream.generate(() -> read(byteChannel))
                .takeWhile(inputBuffer -> checkInputBuffer(inputBuffer, byteChannel))
                .flatMap(this::allocateAll);
    }

    @Override
    public int transactionSize() {
        return transactionSize;
    }

    private boolean checkInputBuffer(ByteBuffer inputBuffer, FileChannel byteChannel) {
        if (inputBuffer != null) return true;
        closeByteChannel(byteChannel);
        return false;
    }

    private synchronized void closeByteChannel(FileChannel byteChannel) {
        try {
            byteChannel.close();
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    private Stream<T> allocateAll(ByteBuffer buffer) {
        StackAllocator<T> allocator = StackAllocators.newManaged(transactionSize, buffer, provider);
        IntStream intStream = IntStream.range(0, buffer.remaining() / transactionSize);
        if(INPUTLEDSTREAM_CONCURRENCY_ENABLED.get()) {
            intStream = intStream.sorted().parallel();
        }
        return intStream.mapToObj(index -> allocator.malloc());
    }

    private synchronized ByteBuffer read(FileChannel byteChannel) {
        try {
            if(byteChannel == null) {
                return null;
            }
            final long filePosition = byteChannel.position();
            if (!byteChannel.isOpen() || filePosition >= fileSize) {
                return null;
            }
            final int size = (int) Math.min(bufferSize, fileSize - filePosition) * transactionSize;
            ByteBuffer buffer = allocBuffer(size);
            int bytesRead = byteChannel.read(buffer);
            if (bytesRead <= 0) return null;
            buffer.position(0).limit(bytesRead);
            return buffer;
        } catch (Exception e) {
            Logger.error(e);
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        if (closed.get()) {
            return;
        }
        if (onClose != null) {
            onClose.run();
            onClose = null;
        }
        if(byteChannel != null) {
            byteChannel.close();
        }
        closed.set(true);
    }
}