package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.allocators.stack.StackAllocator;
import io.intino.alexandria.led.allocators.stack.StackAllocators;
import io.intino.alexandria.led.leds.InputLedStream;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import static io.intino.alexandria.led.util.MemoryUtils.allocBuffer;
import static java.nio.file.StandardOpenOption.*;

public class UnsortedLedStreamBuilder<T extends Transaction> implements LedStream.Builder<T> {

    private static final int DEFAULT_NUM_ELEMENTS_PER_BLOCK = 10_000;


    private final Class<T> transactionClass;
    private final int transactionSize;
    private final TransactionFactory<T> factory;
    private final Path tempDirectory;
    private Path tempLedFile;
    private ByteBuffer buffer;
    private StackAllocator<T> allocator;
    private FileChannel fileChannel;

    public UnsortedLedStreamBuilder(Class<T> transactionClass, File tempDirectory) {
        this(transactionClass, Transaction.factoryOf(transactionClass),
                DEFAULT_NUM_ELEMENTS_PER_BLOCK, tempDirectory);
    }

    public UnsortedLedStreamBuilder(Class<T> transactionClass, TransactionFactory<T> factory,
                                    int numElementsPerBlock, File tempDirectory) {
        this.transactionClass = transactionClass;
        this.transactionSize = Transaction.sizeOf(transactionClass);
        this.factory = factory;
        this.tempDirectory = tempDirectory.toPath();
        if(numElementsPerBlock % 2 != 0) {
            throw new IllegalArgumentException("NumElementsPerBlock must be even");
        }
        buffer = allocBuffer(numElementsPerBlock * transactionSize);
        this.allocator = StackAllocators.newManaged(transactionSize, buffer, factory);
        createTempFile();
    }

    private void createTempFile() {
        try {
            Files.createDirectories(tempDirectory);
            tempLedFile = Files.createTempFile(tempDirectory, transactionClass.getSimpleName(), "_u.led.tmp");
            fileChannel = FileChannel.open(tempLedFile, WRITE, APPEND);
        } catch(Exception e) {
            Logger.error(e);
        }
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
        T transaction = allocator.calloc();
        initializer.accept(transaction);
        if(allocator.remainingBytes() == 0) {
            writeCurrentBlockAndClear();
        }
        return this;
    }

    private void writeCurrentBlockAndClear() {
        try {
            buffer.limit((int) allocator.stackPointer());
            while(buffer.hasRemaining()) {
                fileChannel.write(buffer);
            }
            buffer.clear();
            allocator.clear();
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    @Override
    public LedStream<T> build() {
        free();
        return new InputLedStream<>(getInputStream(), factory, transactionSize)
                .onClose(this::deleteTempFile);
    }

    private void deleteTempFile() {
        tempLedFile.toFile().delete();
        tempLedFile.toFile().deleteOnExit();
    }

    private void free() {
        try {
            allocator.free();
            allocator = null;
            buffer = null;
            fileChannel.close();
            fileChannel = null;
        } catch(Exception e) {
            Logger.error(e);
        }
    }

    private InputStream getInputStream() {
        try {
            return Files.newInputStream(tempLedFile);
        } catch (IOException e) {
            Logger.error(e);
            throw new RuntimeException(e);
        }
    }
}
