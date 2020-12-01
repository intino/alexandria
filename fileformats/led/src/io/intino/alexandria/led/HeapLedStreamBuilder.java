package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.allocators.stack.SingleStackAllocator;
import io.intino.alexandria.led.allocators.stack.StackAllocator;
import io.intino.alexandria.led.buffers.store.ByteBufferStore;
import io.intino.alexandria.led.util.memory.ModifiableMemoryAddress;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static io.intino.alexandria.led.util.memory.MemoryUtils.allocBuffer;

public final class HeapLedStreamBuilder<T extends Transaction> implements LedStream.Builder<T> {

    private static final int DEFAULT_NUM_TRANSACTIONS_PER_BLOCK = 5_000_000;
    private static final File SYSTEM_TEMP_DIR = new File(System.getProperty("java.io.tmpdir"));

    private final int transactionSize;
    private final Class<T> transactionClass;
    private final TransactionFactory<T> factory;
    private final List<Path> tempLeds;
    private final Path tempDirectory;
    private ByteBuffer buffer;
    private StackAllocator<T> allocator;
    private Queue<T> sortedQueue;
    private volatile boolean buildInvoked;

    public HeapLedStreamBuilder(Class<T> transactionClass) {
        this(transactionClass, Transaction.factoryOf(transactionClass));
    }

    public HeapLedStreamBuilder(Class<T> transactionClass, File tempDirectory) {
        this(transactionClass, Transaction.factoryOf(transactionClass), tempDirectory);
    }

    public HeapLedStreamBuilder(Class<T> transactionClass, int numTransactionsPerBlock) {
        this(transactionClass, Transaction.factoryOf(transactionClass), numTransactionsPerBlock);
    }

    public HeapLedStreamBuilder(Class<T> transactionClass, int numTransactionsPerBlock, File tempDirectory) {
        this(transactionClass, Transaction.factoryOf(transactionClass), numTransactionsPerBlock, tempDirectory);
    }

    public HeapLedStreamBuilder(Class<T> transactionClass, TransactionFactory<T> factory) {
        this(transactionClass, factory, DEFAULT_NUM_TRANSACTIONS_PER_BLOCK);
    }

    public HeapLedStreamBuilder(Class<T> transactionClass, TransactionFactory<T> factory, File tempDirectory) {
        this(transactionClass, factory, DEFAULT_NUM_TRANSACTIONS_PER_BLOCK, tempDirectory);
    }

    public HeapLedStreamBuilder(Class<T> transactionClass, TransactionFactory<T> factory, int numTransactionsPerBlock) {
        this(transactionClass, factory, numTransactionsPerBlock, SYSTEM_TEMP_DIR);
    }

    public HeapLedStreamBuilder(Class<T> transactionClass, TransactionFactory<T> factory,
                                int numTransactionsPerBlock, File tempDirectory) {
        this.transactionClass = transactionClass;
        this.transactionSize = Transaction.sizeOf(transactionClass);
        this.factory = factory;
        tempDirectory.mkdirs();
        this.tempDirectory = tempDirectory.toPath();
        tempLeds = new ArrayList<>();
        tempLeds.add(createTempFile());
        buffer = allocBuffer(numTransactionsPerBlock * transactionSize);
        ModifiableMemoryAddress address = ModifiableMemoryAddress.of(buffer);
        ByteBufferStore store = new ByteBufferStore(buffer, address, 0, buffer.capacity());
        allocator = new SingleStackAllocator<>(store, address, transactionSize, factory);
        sortedQueue = new PriorityQueue<>(numTransactionsPerBlock);
    }

    public Path tempDirectory() {
        return tempDirectory;
    }

    private String getTempFilePrefix() {
        return transactionClass.getSimpleName();
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
    public LedStream.Builder<T> append(Consumer<T> initializer) {
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
            return Files.createTempFile(tempDirectory, getTempFilePrefix(), ".led.tmp");
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
        sortedQueue.iterator();
        ledWriter.write(LedStream.fromStream(transactionSize, getSortedTransactions()));
        sortedQueue.clear();
        buffer.clear();
        allocator.clear();
    }

    private Stream<T> getSortedTransactions() {
        return Stream.generate(() -> sortedQueue.poll())
                .takeWhile(Objects::nonNull);
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
            if(Files.exists(tempLedFile)) {
                tempLedFile.toFile().delete();
                tempLedFile.toFile().deleteOnExit();
            }
        }
        tempLeds.clear();
    }

    private LedStream<T> read(Path path) {
        return new LedReader(path.toFile()).read(factory);
    }

    private void freeBuildBuffer() {
        allocator.free();
        buffer = null;
        allocator = null;
        sortedQueue.clear();
        sortedQueue = null;
    }
}
