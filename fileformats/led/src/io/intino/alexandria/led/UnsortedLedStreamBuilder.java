package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.allocators.stack.StackAllocator;
import io.intino.alexandria.led.allocators.stack.StackAllocators;
import io.intino.alexandria.led.leds.InputLedStream;
import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static io.intino.alexandria.led.util.memory.MemoryUtils.allocBuffer;
import static java.nio.file.StandardOpenOption.*;

public class UnsortedLedStreamBuilder<T extends Schema> implements LedStream.Builder<T>, AutoCloseable {

    private static final int DEFAULT_NUM_ELEMENTS_PER_BLOCK = 500_000;


    private final Class<T> schemaClass;
    private final int schemaSize;
    private final SchemaFactory<T> factory;
    private final Path tempLedFile;
    private final UUID serialUUID;
    private ByteBuffer buffer;
    private StackAllocator<T> allocator;
    private FileChannel fileChannel;
    private long numTransactions;
    private final boolean keepFileChannelOpen;
    private final AtomicBoolean closed;

    public UnsortedLedStreamBuilder(Class<T> schemaClass, File tempFile) {
        this(schemaClass, Schema.factoryOf(schemaClass),
                DEFAULT_NUM_ELEMENTS_PER_BLOCK, tempFile, true);
    }

    public UnsortedLedStreamBuilder(Class<T> schemaClass, File tempFile, boolean keepFileChannelOpen) {
        this(schemaClass, Schema.factoryOf(schemaClass),
                DEFAULT_NUM_ELEMENTS_PER_BLOCK, tempFile, keepFileChannelOpen);
    }

    public UnsortedLedStreamBuilder(Class<T> schemaClass, SchemaFactory<T> factory,
                                    int numElementsPerBlock, File tempFile) {
        this(schemaClass, factory, numElementsPerBlock, tempFile, true);
    }

    public UnsortedLedStreamBuilder(Class<T> schemaClass, SchemaFactory<T> factory,
                                    int numElementsPerBlock, File tempFile, boolean keepFileChannelOpen) {
        this.schemaClass = schemaClass;
        this.schemaSize = Schema.sizeOf(schemaClass);
        this.serialUUID = Schema.getSerialUUID(schemaClass);
        this.factory = factory;
        tempFile.getParentFile().mkdirs();
        this.tempLedFile = tempFile.toPath();
        if(numElementsPerBlock % 2 != 0) {
            throw new IllegalArgumentException("NumElementsPerBlock must be even");
        }
        buffer = allocBuffer((long) numElementsPerBlock * schemaSize);
        this.allocator = StackAllocators.managedStackAllocatorFromBuffer(schemaSize, buffer, schemaClass);
        this.keepFileChannelOpen = keepFileChannelOpen;
        this.closed = new AtomicBoolean(false);
        setupFile();
    }

    private void setupFile() {
        try {
            Files.createFile(tempLedFile);
            if(keepFileChannelOpen) {
                fileChannel = openFileChannel();
            }
            reserveHeader();
        } catch(Exception e) {
            Logger.error(e);
        }
    }

    public File tempLedFile() {
        return tempLedFile.toFile();
    }

    private FileChannel openFileChannel() throws IOException {
        return FileChannel.open(tempLedFile, WRITE, APPEND);
    }

    private void reserveHeader() throws IOException {
        if(!keepFileChannelOpen) {
            fileChannel = openFileChannel();
        }
        fileChannel.write(ByteBuffer.allocate(LedHeader.SIZE));
        if(!keepFileChannelOpen) {
            fileChannel.close();
        }
    }

    @Override
    public Class<T> schemaClass() {
        return schemaClass;
    }

    @Override
    public int schemaSize() {
        return schemaSize;
    }

    @Override
    public LedStream.Builder<T> append(Consumer<T> initializer) {
        if(isClosed()) {
            Logger.error("Trying to use a closed builder.");
            return this;
        }
        T schema = allocator.calloc();
        initializer.accept(schema);
        if(allocator.remainingBytes() == 0) {
            writeCurrentBlockAndClear();
        }
        ++numTransactions;
        return this;
    }

    private void writeCurrentBlockAndClear() {
        try {
            if(!keepFileChannelOpen) {
                fileChannel = openFileChannel();
            }
            buffer.limit((int) allocator.stackPointer());
            while(buffer.hasRemaining()) {
                fileChannel.write(buffer);
            }
            if(!keepFileChannelOpen) {
                fileChannel.close();
            }
            buffer.clear();
            allocator.clear();
        } catch (IOException e) {
            Logger.error(e);
            throw new RuntimeException(e);
        }
    }

    public boolean isClosed() {
        return closed.get();
    }

    public synchronized void flush() {
        if(isClosed()) {
            return;
        }
        writeCurrentBlockAndClear();
    }

    @Override
    public synchronized void close() {
        if(closed.compareAndSet(false, true)) {
            writeCurrentBlockAndClear();
            free();
            writeHeader();
        }
    }

    @Override
    public synchronized LedStream<T> build() {
        if(closed.get()) {
            Logger.warn("Trying to call build over a closed " + getClass().getSimpleName() + "...");
            return LedStream.empty(schemaClass);
        }
        close();
        return new InputLedStream<>(getInputStream(), factory, schemaSize)
                .onClose(this::deleteTempFile);
    }

    private void writeHeader() {
        LedHeader header = new LedHeader();
        header.elementCount(numTransactions);
        header.elementSize(schemaSize);
        header.uuid(serialUUID);
        try(RandomAccessFile file = new RandomAccessFile(tempLedFile.toFile(), "rw")) {
            file.writeLong(header.elementCount());
            file.writeInt(header.elementSize());
        } catch (IOException e) {
            Logger.error(e);
            throw new RuntimeException(e);
        }
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
            throw new RuntimeException(e);
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
