package io.intino.alexandria.led.util.sorting;

import io.intino.alexandria.led.*;
import io.intino.alexandria.led.allocators.stack.StackAllocator;
import io.intino.alexandria.led.allocators.stack.StackAllocators;
import io.intino.alexandria.led.util.iterators.MergedIterator;
import io.intino.alexandria.led.util.iterators.StatefulIterator;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static io.intino.alexandria.led.util.memory.MemoryUtils.*;
import static io.intino.alexandria.led.util.memory.MemoryUtils.memcpy;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.Objects.requireNonNull;

// Memory used = numTransactionsInMemory * transactionSize + priorityQueue of numTransactionsInMemory
public class LedExternalMergeSort {

    private static final int DEFAULT_NUM_TRANSACTIONS_IN_MEMORY = 100_000;

    private double start = 0;

    public LedExternalMergeSort() {
    }

    public void sort(File srcFile, File destFile) {
        sort(srcFile.getParentFile(), srcFile, destFile, DEFAULT_NUM_TRANSACTIONS_IN_MEMORY);
    }

    public void sort(File srcFile, File destFile, int numTransactionsInMemory) {
        sort(srcFile.getParentFile(), srcFile, destFile, numTransactionsInMemory);
    }

    public void sort(File tempDirectory, File srcFile, File destFile, int numTransactionsInMemory) {
        try {
            //Logger.info("Starting external merge sort: " + srcFile + " -> " + destFile + "(using " + numTransactionsInMemory + " transactions in memory)...");
            start = System.currentTimeMillis();
            final ChunkCreationInfo chunkCreationInfo = createSortedChunks(tempDirectory, srcFile, numTransactionsInMemory);
            if(chunkCreationInfo == null) {
                handleEmptyFile(destFile);
            } else {
                mergeSortedChunks(chunkCreationInfo, destFile);
            }
            //Logger.info("External merge sort finished after " + time());
        } catch(Exception e) {
            Logger.error(e);
        }
    }

    private void handleEmptyFile(File destFile) {
        LedStream.empty().serialize(destFile);
    }

    private ChunkCreationInfo createSortedChunks(File tempDir, File srcFile, int numTransactionsInMemory) {
        //System.out.println(">> Creating sorted chunks... " + time());
        try(FileChannel fileChannel = FileChannel.open(srcFile.toPath(), READ)) {
            final long fileSize = fileChannel.size();
            final Path chunkDir = Files.createDirectory(tempDir.toPath()
                    .resolve("ChunksFolder_"+System.nanoTime()));
            final String srcFileName = srcFile.getName();
            LedHeader header = requireNonNull(LedHeader.from(fileChannel));
            if(header.elementCount() == 0) {
                // Empty file
                return null;
            }
            final int transactionSize = header.elementSize();
            final long numTransactions = header.elementCount();
            final int chunkSize = numTransactionsInMemory * transactionSize;
            final int numChunks = Math.round(header.elementCount() / (float)chunkSize);
            Queue<Path> chunks = new LinkedList<>();
            ByteBuffer buffer = allocBuffer(chunkSize);
            ByteBuffer tempBuffer = allocBuffer(chunkSize);
            StackAllocator<GenericTransaction> allocator = StackAllocators.newManaged(transactionSize, buffer, GenericTransaction::new);
            PriorityQueue<GenericTransaction> priorityQueue = new PriorityQueue<>(numTransactionsInMemory);

            for(int i = 0;fileChannel.position() < fileSize;i++) {
                Path chunk = Files.createFile(chunkDir.resolve("Chunk["+i+"]_" + srcFileName));
                final int bytesRead = fileChannel.read(buffer.position(0).limit(chunkSize));
                buffer.clear();
                writeSortedChunk(chunk, transactionSize, chunkSize, buffer, tempBuffer, allocator, priorityQueue, bytesRead);
                //checkSorting(chunk, transactionSize);
                chunks.add(chunk);
                priorityQueue.clear();
                allocator.clear();
            }
            priorityQueue.clear();
            allocator.clear();
            free(tempBuffer);
            return new ChunkCreationInfo(header, chunkDir, chunks, chunkSize, buffer);
        } catch (IOException e) {
            Logger.error(e);
            throw new RuntimeException(e);
        }
    }

    private void writeSortedChunk(Path chunk, int transactionSize, int chunkSize, ByteBuffer buffer, ByteBuffer tempBuffer,
                                  StackAllocator<GenericTransaction> allocator, PriorityQueue<GenericTransaction> priorityQueue,
                                  int elementsRead) {
        try(FileChannel fileChannel = FileChannel.open(chunk, WRITE)) {
            //System.out.println(">> Writing chunk " + chunk + "..." + time());
            sortChunk(tempBuffer, allocator, priorityQueue, transactionSize, elementsRead);
            fileChannel.write(tempBuffer);
            tempBuffer.clear();
        } catch (IOException e) {
            Logger.error(e);
            throw new RuntimeException(e);
        }
        //System.out.println(">> Chunk " + chunk + " processed!" + time());
    }

    private void sortChunk(ByteBuffer tempBuffer,
                           StackAllocator<GenericTransaction> allocator, PriorityQueue<GenericTransaction> priorityQueue,
                           int transactionSize, int bytesRead) {

        for(int i = 0;i < bytesRead;i+=transactionSize) {
            priorityQueue.add(allocator.malloc());
        }
        final int limit = priorityQueue.size() * transactionSize;
        final long tempBufferPtr = addressOf(tempBuffer);
        long offset = 0;
        while(!priorityQueue.isEmpty()) {
            GenericTransaction transaction = priorityQueue.poll();
            memcpy(transaction.address() + transaction.baseOffset(), tempBufferPtr + offset, transactionSize);
            offset += transactionSize;
        }
        tempBuffer.position(0).limit(limit);
    }

    private void mergeSortedChunks(ChunkCreationInfo chunkCreationInfo, File destFile) throws IOException {
        //Logger.info("Merging sorted chunks..." + time());
        Queue<Path> chunks = chunkCreationInfo.sortedChunkFiles;
        Path dir = chunkCreationInfo.chunkDirectory;
        if(chunks.size() == 1) {
            writeFinalChunkToDestFile(destFile, chunks.remove(), chunkCreationInfo.ledHeader);
            removeDirectory(dir);
            return;
        }
        ByteBuffer buffer = chunkCreationInfo.buffer();
        final int transactionSize = chunkCreationInfo.ledHeader.elementSize();
        final int chunkSize = chunkCreationInfo.chunkSize / 2;
        ByteBuffer tempBuffer = allocBuffer(chunkCreationInfo.chunkSize);
        int count = 0;
        while(chunks.size() > 2) {
            Path chunk1 = chunks.remove();
            Path chunk2 = chunks.remove();
            Path mergedChunk = Files.createFile(dir.resolve("Chunk_" + count + "_merged_with_" + (count + 1) + ".led.tmp"));
            //Logger.trace("Creating " + mergedChunk + "..." + time());
            MergedIterator<ChunkIterator.TransactionWrapper> mergedIterator = merge(chunk1, chunk2, buffer,
                    transactionSize, chunkSize);
            writeSortedToMergedChunk(mergedChunk, mergedIterator, tempBuffer);
            deleteChunks(chunk1, chunk2);
            chunks.add(mergedChunk);
            count += 2;
        }
        //Logger.info("Running final merge..." + time());
        free(chunkCreationInfo.buffer);

        if(chunks.size() == 2) {
            doFinalMergeAndWriteToDestFile(destFile, chunks.remove(), chunks.remove(), chunkCreationInfo.ledHeader);
        } else if(chunks.size() == 1) {
            writeFinalChunkToDestFile(destFile, chunks.remove(), chunkCreationInfo.ledHeader);
        } else {
            throw new IllegalStateException("Number of chunks is neither 1 or 2.");
        }
    }

    private void removeDirectory(Path dir) {
        try {
            Files.delete(dir);
            File dirFile = dir.toFile();
            dirFile.delete();
            dirFile.deleteOnExit();
        } catch (IOException e) {
            Logger.warn(e.getMessage());
        }
    }

    private void deleteChunks(Path... chunks) {
        for(Path chunk : chunks) {
            final File chunkFile = chunk.toFile();
            chunkFile.delete();
            chunkFile.deleteOnExit();
        }
    }

    private void checkSorting(Path chunk, int transactionSize) {
        //Logger.trace("Validating sorted chunk " + chunk + "...");
        try(LedStream<?> ledStream = readChunk(chunk, transactionSize)) {
            Long previousId = null;
            while(ledStream.hasNext()) {
                Transaction transaction = ledStream.next();
                final long id = Transaction.idOf(transaction);
                if(previousId != null && id < previousId) {
                    throw new AssertionError(chunk + " is unsorted: " + previousId + " > " + Transaction.idOf(transaction));
                }
                previousId = id;
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    private void doFinalMergeAndWriteToDestFile(File destFile, Path chunk1, Path chunk2, LedHeader ledHeader) {

        final int elementSize = ledHeader.elementSize();
        LedStream.merged(Stream.of(readChunk(chunk1, elementSize), readChunk(chunk2, elementSize))).serialize(destFile);
        deleteChunks(chunk1, chunk2);
        deleteDir(chunk1.getParent());
    }

    private void writeFinalChunkToDestFile(File destFile, Path chunk, LedHeader ledHeader) {
        final int elementSize = ledHeader.elementSize();
        LedStream.merged(Stream.of(readChunk(chunk, elementSize))).serialize(destFile);
        deleteChunks(chunk);
    }

    private void deleteDir(Path dir) {
        File dirFile = dir.toFile();
        dirFile.delete();
        dirFile.deleteOnExit();
    }

    private LedStream<GenericTransaction> readChunk(Path chunk, int elementSize) {
        try {
            return new LedReader(Files.newInputStream(chunk)).readUncompressed(elementSize, GenericTransaction::new);
        } catch (IOException e) {
            Logger.error(e);
            throw new RuntimeException(e);
        }
    }

    private void writeSortedToMergedChunk(Path mergedChunk, MergedIterator<ChunkIterator.TransactionWrapper> iterator,
                                          ByteBuffer tempBuffer) {
        try(FileChannel fileChannel = FileChannel.open(mergedChunk, WRITE)) {
            final long tempBufferPtr = addressOf(tempBuffer);
            long offset = 0;
            while(iterator.hasNext()) {
                ChunkIterator.TransactionWrapper transaction = iterator.next();
                ByteBuffer buffer = transaction.buffer();
                memcpy(addressOf(buffer) + transaction.offset, tempBufferPtr + offset, transaction.size());
                offset += transaction.size();
                if(offset == tempBuffer.capacity()) {
                    fileChannel.write(tempBuffer);
                    tempBuffer.clear();
                    offset = 0;
                }
            }
        } catch (IOException e) {
            Logger.error(e);
            throw new RuntimeException(e);
        }
    }

    private MergedIterator<ChunkIterator.TransactionWrapper> merge(Path chunk1, Path chunk2, ByteBuffer buffer, int elementSize, int chunkSize) {
        return new MergedIterator<>(Stream.of(
                new ChunkIterator(chunk1, buffer, elementSize, 0, chunkSize),
                new ChunkIterator(chunk2, buffer, elementSize, chunkSize, chunkSize)),
                Comparator.naturalOrder());
    }

    private String time() {
        double time = System.currentTimeMillis() - start;
        return " " + time / 1000.0 + " seconds";
    }

    private static class ChunkIterator implements StatefulIterator<ChunkIterator.TransactionWrapper> {

        private final FileChannel fileChannel;
        private final int transactionSize;
        private final long fileSize;
        private final ByteBuffer buffer;
        private TransactionWrapper current;
        private long filePosition;
        private int bufferPosition;
        private final int bufferBaseOffset;
        private final int chunkSize;
        private boolean requestReadNextChunk;
        private int transactionPosition;

        public ChunkIterator(Path file, ByteBuffer buffer, int transactionSize, int bufferBaseOffset, int chunkSize) {
            this.bufferBaseOffset = bufferBaseOffset;
            try {
                this.fileChannel = open(file);
                this.fileSize = fileChannel.size();
                this.buffer = buffer;
                this.chunkSize = chunkSize;
                this.transactionSize = transactionSize;
                readNextChunk();
            } catch (Exception e) {
                Logger.error(e);
                throw new RuntimeException(e);
            }
        }

        @Override
        public TransactionWrapper current() {
            return current;
        }

        @Override
        public boolean hasNext() {
            return transactionPosition < chunkSize;
        }

        @Override
        public TransactionWrapper next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            if(requestReadNextChunk) {
                readNextChunk();
                requestReadNextChunk = false;
            }
            current = new TransactionWrapper(bufferBaseOffset + bufferPosition);
            advanceToNextElement();
            return current;
        }

        private void advanceToNextElement() {
            if(bufferPosition >= chunkSize && filePosition < fileSize) {
                // Must read next chunk from file
                // Do it in the next call to next() to avoid overriding current transaction data
                requestReadNextChunk = true;
            } else {
                bufferPosition += transactionSize;
                transactionPosition += transactionSize;
            }
        }

        private void readNextChunk() {
            if(filePosition >= fileSize) {
                throw new IndexOutOfBoundsException();
            }
            try {
                bufferPosition = 0;
                buffer.position(bufferBaseOffset).limit(bufferBaseOffset + chunkSize);
                filePosition += fileChannel.read(buffer);
                buffer.clear();
                current = null;
            } catch (IOException e) {
                Logger.error(e);
                throw new RuntimeException(e);
            }
        }

        private FileChannel open(Path file) throws IOException {
            return FileChannel.open(file, READ);
        }

        private final class TransactionWrapper implements Comparable<TransactionWrapper> {

            private final int offset;

            public TransactionWrapper(int offset) {
                this.offset = offset;
            }

            public long id() {
                return buffer.getLong(offset);
            }

            public int size() {
                return transactionSize;
            }

            public ByteBuffer buffer() {
                return buffer;
            }

            @Override
            public int compareTo(TransactionWrapper o) {
                return Long.compare(id(), o.id());
            }
        }
    }

    private static final class ChunkCreationInfo {

        private final LedHeader ledHeader;
        private final Path chunkDirectory;
        private final Queue<Path> sortedChunkFiles;
        private final int chunkSize;
        private final ByteBuffer buffer;

        public ChunkCreationInfo(LedHeader ledHeader, Path chunkDirectory, Queue<Path> sortedChunkFiles, int chunkSize, ByteBuffer buffer) {
            this.ledHeader = ledHeader;
            this.chunkDirectory = chunkDirectory;
            this.sortedChunkFiles = sortedChunkFiles;
            this.chunkSize = chunkSize;
            this.buffer = buffer;
        }

        public ByteBuffer buffer() {
            return buffer.position(0).limit(chunkSize);
        }
    }

}
