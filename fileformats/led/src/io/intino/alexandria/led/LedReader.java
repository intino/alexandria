package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocator;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocatorFactory;
import io.intino.alexandria.led.leds.IndexedLed;
import io.intino.alexandria.led.leds.InputLedStream;
import io.intino.alexandria.logger.Logger;
import org.xerial.snappy.SnappyInputStream;

import java.io.*;

import static io.intino.alexandria.led.util.memory.MemoryUtils.allocBuffer;
import static io.intino.alexandria.led.util.memory.MemoryUtils.memcpy;

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

	public <T extends Transaction> Led<T> readAll(TransactionFactory<T> factory) {
		return readAll(getDefaultAllocatorFactory(), factory);
	}

	public <T extends Transaction> Led<T> readAll(IndexedAllocatorFactory<T> allocatorFactory, TransactionFactory<T> factory) {
		try {
			if(source.available() == 0) {
				return Led.empty();
			}
		} catch(Exception e) {
			Logger.error(e);
			return Led.empty();
		}
		LedHeader header = LedHeader.from(this.source);
		try(SnappyInputStream inputStream = new SnappyInputStream(this.source)) {
			IndexedAllocator<T> allocator = allocatorFactory.create(inputStream, header.elementCount(), header.elementSize(), factory);
			return new IndexedLed<>(allocator);
		} catch (IOException e) {
			Logger.error(e);
		}
		return Led.empty();
	}

	public <T extends Transaction> LedStream<T> read(TransactionFactory<T> factory) {
		try {
			if(source.available() == 0) {
				return LedStream.empty();
			}
			LedHeader header = LedHeader.from(source);
			return allocate(new SnappyInputStream(source), factory, header.elementSize());
		} catch (IOException e) {
			Logger.error(e);
		}
		return LedStream.empty();
	}

	public <T extends Transaction> LedStream<T> readUncompressed(int elementSize, TransactionFactory<T> factory) {
		try {
			if(source.available() == 0) {
				return LedStream.empty();
			}
			return allocate(source, factory, elementSize);
		} catch (IOException e) {
			Logger.error(e);
		}
		return LedStream.empty();
	}

	private <T extends Transaction> LedStream<T> allocate(InputStream inputStream, TransactionFactory<T> factory, int transactionSize) {
		return new InputLedStream<>(inputStream, factory, transactionSize);
	}

	private static InputStream inputStreamOf(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			Logger.error("Failed to create FileInputStream for file " + file +
					". Probably too many files has been opened.", e);
			return new ByteArrayInputStream(new byte[0]);
		}
	}

	private <T extends Transaction> IndexedAllocatorFactory<T> getDefaultAllocatorFactory() {
		return (inputStream, elementCount, elementSize, factory) -> {
			if(elementCount >= 0 && elementCount * elementSize < Integer.MAX_VALUE) {
				return IndexedAllocatorFactory.newManagedIndexedAllocator(inputStream, elementCount, elementSize, factory);
			}
			return IndexedAllocatorFactory.newArrayAllocator(inputStream, elementCount, elementSize, factory);
		};
	}

}