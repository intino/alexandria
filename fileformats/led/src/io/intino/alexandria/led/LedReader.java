package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocator;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocatorFactory;
import io.intino.alexandria.led.leds.IndexedLed;
import io.intino.alexandria.led.leds.InputLedStream;
import io.intino.alexandria.logger.Logger;
import org.xerial.snappy.SnappyInputStream;

import java.io.*;

import static io.intino.alexandria.led.util.MemoryUtils.allocBuffer;
import static io.intino.alexandria.led.util.MemoryUtils.memcpy;

public class LedReader {

	private final InputStream source;
	private final File sourceFile;

	public LedReader(File file) {
		this.source = inputStreamOf(file);
		this.sourceFile = file;
	}

	public LedReader(InputStream source) {
		this.source = source;
		this.sourceFile = null;
	}

	public int size() {
		if(sourceFile == null) {
			return (int) LedHeader.UNKNOWN_SIZE;
		}
		try(RandomAccessFile raFile = new RandomAccessFile(sourceFile, "r")) {
			return (int) raFile.readLong();
		} catch (IOException e) {
			Logger.error(e);
		}
		return (int) LedHeader.UNKNOWN_SIZE;
	}

	public <S extends Transaction> Led<S> readAll(TransactionFactory<S> factory) {
		return readAll(getDefaultAllocatorFactory(), factory);
	}

	public <S extends Transaction> Led<S> readAll(IndexedAllocatorFactory<S> allocatorFactory, TransactionFactory<S> factory) {
		LedHeader header = LedHeader.from(this.source);
		try(SnappyInputStream inputStream = new SnappyInputStream(this.source)) {
			IndexedAllocator<S> allocator = allocatorFactory.create(inputStream, header.elementCount(), header.elementSize(), factory);
			return new IndexedLed<>(allocator);
		} catch (IOException e) {
			Logger.error(e);
		}
		return null;
	}

	public <S extends Transaction> LedStream<S> read(TransactionFactory<S> factory) {
		try {
			LedHeader header = LedHeader.from(source);
			return allocate(new SnappyInputStream(source), factory, header.elementSize());
		} catch (IOException e) {
			Logger.error(e);
		}
		return null;
	}

	private <S extends Transaction> LedStream<S> allocate(SnappyInputStream inputStream, TransactionFactory<S> factory, int transactionSize) {
		return new InputLedStream<>(inputStream, factory, transactionSize);
	}

	private static InputStream inputStreamOf(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			return new ByteArrayInputStream(new byte[0]);
		}
	}

	private <S extends Transaction> IndexedAllocatorFactory<S> getDefaultAllocatorFactory() {
		return (inputStream, elementCount, elementSize, factory) -> {

			if(elementCount >= 0 && elementCount * elementSize < Integer.MAX_VALUE) {
				return IndexedAllocatorFactory.newManagedIndexedAllocator(inputStream, elementCount, elementSize, factory);
			}

			return IndexedAllocatorFactory.newArrayAllocator(inputStream, elementCount, elementSize, factory);
		};
	}

}