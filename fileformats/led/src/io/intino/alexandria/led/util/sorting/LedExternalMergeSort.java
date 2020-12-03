package io.intino.alexandria.led.util.sorting;

import io.intino.alexandria.led.*;
import io.intino.alexandria.led.allocators.stack.StackAllocator;
import io.intino.alexandria.led.allocators.stack.StackAllocators;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Stream;

import static io.intino.alexandria.led.util.memory.MemoryUtils.*;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

public class LedExternalMergeSort {

    private static final int DEFAULT_NUM_TRANSACTIONS_IN_MEMORY = 100_000;

    private final File srcFile;
    private final File destFile;
    private File chunksDirectory;
    private int numTransactionsInMemory;
    private boolean debug;
    private boolean checkChunkSorting;
    private ByteBuffer primaryBuffer;
    private ByteBuffer secondaryBuffer;
    private LedHeader ledHeader;
    private Queue<Path> chunks;
    private boolean deleteChunkDirOnExit;
    private double startTime = 0;

    public LedExternalMergeSort(File srcFile, File destFile) {
        this.srcFile = srcFile;
        this.destFile = destFile;
        destFile.getParentFile().mkdirs();
        File defaultChunkDir = new File(srcFile.getParentFile(), "Chunks_Dir_" + System.nanoTime());
        chunksDirectory(defaultChunkDir);
        numTransactionsInMemory(DEFAULT_NUM_TRANSACTIONS_IN_MEMORY);
    }

    public int maxMemoryUsed(int transactionSize) {
        return numTransactionsInMemory * transactionSize + numTransactionsInMemory * 32;
    }

    public int numTransactionsInMemory() {
        return numTransactionsInMemory;
    }

    public LedExternalMergeSort numTransactionsInMemory(int numTransactionsInMemory) {
        this.numTransactionsInMemory = Math.max(1000, numTransactionsInMemory);
        return this;
    }

    public File chunksDirectory() {
        return chunksDirectory;
    }

    public LedExternalMergeSort chunksDirectory(File chunksDirectory) {
        this.chunksDirectory = chunksDirectory;
        chunksDirectory.mkdirs();
        return this;
    }

    public boolean debug() {
        return debug;
    }

    public LedExternalMergeSort debug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public boolean checkChunkSorting() {
        return checkChunkSorting;
    }

    public LedExternalMergeSort checkChunkSorting(boolean checkChunkSorting) {
        this.checkChunkSorting = checkChunkSorting;
        return this;
    }

    private int transactionSize() {
        return ledHeader.elementSize();
    }

    private long transactionsCount() {
        return ledHeader.elementCount();
    }

    public boolean deleteChunkDirOnExit() {
        return deleteChunkDirOnExit;
    }

    public LedExternalMergeSort deleteChunkDirOnExit(boolean deleteChunkDirOnExit) {
        this.deleteChunkDirOnExit = deleteChunkDirOnExit;
        return this;
    }

    public void sort() {
        try {
            if(debug) {
                Logger.info("Starting external merge sort: " + srcFile + " -> " + destFile + "(using "
                        + numTransactionsInMemory + " transactions in memory)...");
            }
            startTime = System.currentTimeMillis();
            createSortedChunks();
            if(transactionsCount() == 0) {
                handleEmptyFile();
            } else {
                mergeSortedChunks();
            }
            if(debug) {
                Logger.info("External merge sort finished after " + time());
            }
        } catch(Exception e) {
            Logger.error(e);
        } finally {
            freeBuffers();
            chunks = null;
            ledHeader = null;
            if(deleteChunkDirOnExit) {
                chunksDirectory.delete();
                chunksDirectory.deleteOnExit();
            }
        }
    }

    private String time() {
        final double time = (System.currentTimeMillis() - startTime) / 1000.0;
        return time + " minutes";
    }

    private void handleEmptyFile() {
        LedStream.empty().serialize(destFile);
    }

    private void createSortedChunks() {

        try(FileChannel fileChannel = FileChannel.open(srcFile.toPath(), READ)) {

            final long fileSize = fileChannel.size();
            final String srcFileName = srcFile.getName();
            ledHeader = LedHeader.from(fileChannel);
            if(ledHeader == null || ledHeader.elementCount() == 0) {
                // Empty file
                if(debug) {
                    Logger.info("File " + srcFileName + " is empty. Not merge sorting.");
                }
            }
            final int transactionSize = ledHeader.elementSize();
            final int bufferSize = (numTransactionsInMemory * transactionSize) / 2;
            if(debug) {
                Logger.info("Buffer Size = " + bufferSize / 1024.0 / 1024.0 + " MB");
            }

            allocateBuffers(bufferSize);

            chunks = new LinkedList<>();

            StackAllocator<GenericTransaction> allocator = createAllocator(primaryBuffer);
            PriorityQueue<GenericTransaction> priorityQueue = new PriorityQueue<>(numTransactionsInMemory);

            Path chunkDir = chunksDirectory.toPath();

            for(int i = 0;fileChannel.position() < fileSize;i++) {
                Path chunk = Files.createFile(chunkDir.resolve("Chunk["+i+"]_" + srcFileName));
                clearBuffers();
                final int bytesRead = fileChannel.read(primaryBuffer);
                clearBuffers();
                writeSortedChunk(chunk, transactionSize, allocator, priorityQueue, bytesRead);
                if(checkChunkSorting) {
                    checkSorting(chunk, transactionSize);
                }
                chunks.add(chunk);
                priorityQueue.clear();
                allocator.clear();
            }
            priorityQueue.clear();
            allocator.clear();
        } catch (IOException e) {
            Logger.error(e);
            throw new RuntimeException(e);
        }
    }

    private void clearBuffers() {
        primaryBuffer.clear();
        secondaryBuffer.clear();
    }

    private void allocateBuffers(int bufferSize) {
        primaryBuffer = allocBuffer(bufferSize);
        secondaryBuffer = allocBuffer(bufferSize);
    }

    private StackAllocator<GenericTransaction> createAllocator(ByteBuffer buffer) {
        return StackAllocators.newManaged(transactionSize(), buffer, GenericTransaction::new);
    }

    private void writeSortedChunk(Path chunk, int transactionSize,
                                  StackAllocator<GenericTransaction> allocator,
                                  PriorityQueue<GenericTransaction> priorityQueue,
                                  int elementsRead) {
        try(FileChannel fileChannel = FileChannel.open(chunk, WRITE)) {
            sortChunk(allocator, priorityQueue, transactionSize, elementsRead);
            fileChannel.write(secondaryBuffer);
        } catch (IOException e) {
            Logger.error(e);
            throw new RuntimeException(e);
        }
    }

    private void sortChunk(StackAllocator<GenericTransaction> allocator,
                           PriorityQueue<GenericTransaction> priorityQueue,
                           int transactionSize, int bytesRead) {

        for(int i = 0;i < bytesRead;i+=transactionSize) {
            priorityQueue.add(allocator.malloc());
        }
        final int limit = priorityQueue.size() * transactionSize;
        final long tempBufferPtr = addressOf(secondaryBuffer);
        long offset = 0;
        while(!priorityQueue.isEmpty()) {
            GenericTransaction transaction = priorityQueue.poll();
            memcpy(transaction.address() + transaction.baseOffset(), tempBufferPtr + offset, transactionSize);
            offset += transactionSize;
        }
        secondaryBuffer.position(0).limit(limit);
    }

    private void mergeSortedChunks() throws IOException {
        //Logger.info("Merging sorted chunks..." + time());
        if(chunks.size() == 1) {
            writeFinalChunkToDestFile(destFile, chunks.remove(), ledHeader);
            removeDirectory(chunksDirectory);
            return;
        }
        final int transactionSize = transactionSize();
        //ByteBuffer tempBuffer = allocBuffer(chunkCreationInfo.chunkSize);
        int count = 0;

        Path chunksDir = chunksDirectory.toPath();

        while(chunks.size() > 2) {
            Path chunk1 = chunks.remove();
            Path chunk2 = chunks.remove();
            Path mergedChunk = Files.createFile(chunksDir.resolve("Chunk_" + count + "_merged_with_" + (count + 1) + ".led.tmp"));
            merge(chunk1, chunk2, mergedChunk, transactionSize);
            if(checkChunkSorting) {
                checkSorting(mergedChunk, transactionSize);
            }
            deleteChunks(chunk1, chunk2);
            chunks.add(mergedChunk);
            count += 2;
        }

        if(debug) {
            Logger.info("Running final merge..." + time());
        }

        freeBuffers();

        if(chunks.size() == 2) {
            doFinalMergeAndWriteToDestFile(destFile, chunks.remove(), chunks.remove(), ledHeader);
        } else if(chunks.size() == 1) {
            writeFinalChunkToDestFile(destFile, chunks.remove(), ledHeader);
        } else {
            throw new IllegalStateException("Number of chunks is neither 1 or 2.");
        }
    }

    private void freeBuffers() {
        free(primaryBuffer);
        free(secondaryBuffer);
    }

    private void merge(Path chunk1, Path chunk2, Path mergedChunk, int transactionSize) {
        LedStream.merged(Stream.of(readChunk(chunk1, transactionSize), readChunk(chunk2, transactionSize)))
                .serializeUncompressed(mergedChunk.toFile());
    }

    private void removeDirectory(File dir) {
        dir.delete();
        dir.deleteOnExit();
    }

    private void deleteChunks(Path... chunks) {
        for(Path chunk : chunks) {
            final File chunkFile = chunk.toFile();
            chunkFile.delete();
            chunkFile.deleteOnExit();
        }
    }

    private void checkSorting(Path chunk, int transactionSize) {
        try(LedStream<?> ledStream = readChunk(chunk, transactionSize)) {
            long previousId = Long.MIN_VALUE;
            while(ledStream.hasNext()) {
                Transaction transaction = ledStream.next();
                final long id = Transaction.idOf(transaction);
                if(id < previousId) {
                    throw new IllegalStateException(chunk + " is unsorted: " + previousId + " > " + Transaction.idOf(transaction));
                }
                previousId = id;
            }
        } catch(IllegalStateException e) {
            throw e;
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
        readChunk(chunk, elementSize).serialize(destFile);
        deleteChunks(chunk);
    }

    private void deleteDir(Path dir) {
        File dirFile = dir.toFile();
        dirFile.delete();
        dirFile.deleteOnExit();
    }

    private LedStream<GenericTransaction> readChunk(Path chunk, int elementSize) {
        return new LedReader(chunk.toFile()).readUncompressed(elementSize, GenericTransaction::new);
    }

}
