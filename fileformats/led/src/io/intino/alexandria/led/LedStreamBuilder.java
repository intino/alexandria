package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.allocators.stack.SingleStackAllocator;
import io.intino.alexandria.led.allocators.stack.StackAllocator;
import io.intino.alexandria.led.buffers.store.ByteBufferStore;
import io.intino.alexandria.led.util.ModifiableMemoryAddress;
import io.intino.alexandria.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Consumer;

import static io.intino.alexandria.led.util.MemoryUtils.allocBuffer;

public final class LedStreamBuilder<T extends Transaction> implements LedStream.Builder<T> {

    private static final int DEFAULT_NUM_TRANSACTIONS_PER_BLOCK = 5_000_000;
    private static final Path SYSTEM_TEMP_DIR = Paths.get(System.getProperty("java.io.tmpdir"));

    private final int transactionSize;
    private final Class<T> transactionClass;
    private final TransactionFactory<T> factory;
    private final List<Path> tempLeds;
    private ByteBuffer buffer;
    private StackAllocator<T> allocator;
    private Queue<T> sortedQueue;
    private volatile boolean buildInvoked;

    public LedStreamBuilder(Class<T> transactionClass) {
        this(transactionClass, Transaction.factoryOf(transactionClass));
    }

    public LedStreamBuilder(Class<T> transactionClass, int numTransactionsPerBlock) {
        this(transactionClass, Transaction.factoryOf(transactionClass), numTransactionsPerBlock);
    }

    public LedStreamBuilder(Class<T> transactionClass, TransactionFactory<T> factory) {
        this(transactionClass, factory, DEFAULT_NUM_TRANSACTIONS_PER_BLOCK);
    }

    public LedStreamBuilder(Class<T> transactionClass, TransactionFactory<T> factory, int numTransactionsPerBlock) {
        this.transactionClass = transactionClass;
        this.transactionSize = Transaction.sizeOf(transactionClass);
        this.factory = factory;
        tempLeds = new ArrayList<>();
        tempLeds.add(createTempFile());
        buffer = allocBuffer(numTransactionsPerBlock * transactionSize);
        ModifiableMemoryAddress address = ModifiableMemoryAddress.of(buffer);
        ByteBufferStore store = new ByteBufferStore(buffer, address, 0, buffer.capacity());
        allocator = new SingleStackAllocator<>(store, address, transactionSize, factory);
        sortedQueue = new PriorityQueue<>(numTransactionsPerBlock);
    }

    private String getTempFilePrefix() {
        return getClass().getSimpleName() + '.' +
                transactionClass.getSimpleName();
    }

    @Override
    public Class<T> transactionClass() {
        return transactionClass;
    }

    @Override
    public int transactionSize() {
        return transactionSize;
    }

    @Override
    public LedStream.Builder<T> create(Consumer<T> initializer) {
        if(buildInvoked) {
            throw new IllegalStateException("Method build has been called, cannot create more transactions.");
        }
        T transaction = newTransaction();
        initializer.accept(transaction);
        sortedQueue.add(transaction);
        return this;
    }

    private T newTransaction() {
        if(allocator.remainingBytes() <= 0) {
            writeCurrentBlockAndClear();
            tempLeds.add(createTempFile());
        }
        return allocator.calloc();
    }

    private Path createTempFile() {
        try {
            return Files.createTempFile(SYSTEM_TEMP_DIR, getTempFilePrefix(), ".tmp");
        } catch (IOException e) {
            Logger.error(e);
            throw new RuntimeException(e);
        }
    }

    private void writeCurrentBlockAndClear() {
        if(allocator.stackPointer() == 0) {
            return;
        }
        LedWriter ledWriter = new LedWriter(getCurrentFile().toFile());
        ledWriter.write(LedStream.fromStream(transactionSize, sortedQueue.stream()));
        sortedQueue.clear();
        buffer.clear();
        allocator.clear();
    }

    private Path getCurrentFile() {
        return tempLeds.get(tempLeds.size() - 1);
    }

    @Override
    public LedStream<T> build() {
        if(buildInvoked) {
            throw new IllegalStateException("Method build has been already been called.");
        }
        writeCurrentBlockAndClear();
        freeBuildBuffer();
        buildInvoked = true;
        return mergeAllTempLeds();
    }

    private LedStream<T> mergeAllTempLeds() {
        return LedStream.merged(tempLeds.stream()
                .map(this::read))
                .onClose(this::deleteAllTempFiles);
    }

    private void deleteAllTempFiles() {
        for(Path tempLedFile : tempLeds) {
            try {
                Files.delete(tempLedFile);
                tempLedFile.toFile().delete();
                tempLedFile.toFile().deleteOnExit();
            } catch (IOException e) {
                Logger.error(e);
            }
        }
        tempLeds.clear();
    }

    private LedStream<T> read(Path path) {
        return new LedReader(path.toFile()).read(factory);
    }

    private InputStream createInputStream(Path path) {
        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            Logger.error(e);
            throw new RuntimeException(e);
        }
    }

    private void freeBuildBuffer() {
        allocator.free();
        buffer = null;
        allocator = null;
        sortedQueue.clear();
        sortedQueue = null;
    }
}
