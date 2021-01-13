package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocator;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocatorFactory;
import io.intino.alexandria.led.leds.ByteChannelLedStream;
import io.intino.alexandria.led.leds.IndexedLed;
import io.intino.alexandria.led.leds.InputLedStream;
import io.intino.alexandria.logger.Logger;
import org.xerial.snappy.SnappyInputStream;

import java.io.*;

import static io.intino.alexandria.led.util.memory.MemoryUtils.allocBuffer;
import static io.intino.alexandria.led.util.memory.MemoryUtils.memcpy;

public class LedReader {

	private final InputStream srcInputStream;
	private final File sourceFile;

	public LedReader(File file) {
		this.srcInputStream = inputStreamOf(file);
		this.sourceFile = file;
	}

	public LedReader(InputStream srcInputStream) {
		this.srcInputStream = srcInputStream;
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

	public <T extends Schema> Led<T> readAll(SchemaFactory<T> factory) {
		return readAll(getDefaultAllocatorFactory(), factory);
	}

	public <T extends Schema> Led<T> readAll(IndexedAllocatorFactory<T> allocatorFactory, SchemaFactory<T> factory) {
		try {
			if(srcInputStream.available() == 0) {
				return Led.empty();
			}
		} catch(Exception e) {
			Logger.error(e);
			return Led.empty();
		}
		LedHeader header = LedHeader.from(this.srcInputStream);
		try(SnappyInputStream inputStream = new SnappyInputStream(this.srcInputStream)) {
			IndexedAllocator<T> allocator = allocatorFactory.create(inputStream, header.elementCount(), header.elementSize(), factory);
			return new IndexedLed<>(allocator);
		} catch (IOException e) {
			Logger.error(e);
		}
		return Led.empty();
	}

	public <T extends Schema> LedStream<T> read(SchemaFactory<T> factory) {
		try {
			if(srcInputStream.available() == 0) {
				return LedStream.empty();
			}
			LedHeader header = LedHeader.from(srcInputStream);
			return allocate(new SnappyInputStream(srcInputStream), factory, header.elementSize());
		} catch (IOException e) {
			Logger.error(e);
		}
		return LedStream.empty();
	}

	public <T extends Schema> LedStream<T> readUncompressed(int elementSize, SchemaFactory<T> factory) {
		try {
			if(srcInputStream.available() == 0) {
				return LedStream.empty();
			}
			return allocateUncompressed(factory, elementSize);
		} catch (IOException e) {
			Logger.error(e);
		}
		return LedStream.empty();
	}

	private <T extends Schema> LedStream<T> allocateUncompressed(SchemaFactory<T> factory, int elementSize) {
		try {
			srcInputStream.close();
		} catch (IOException e) {
			Logger.error(e);
		}
		return new ByteChannelLedStream<>(sourceFile, factory, elementSize);
	}

	private <T extends Schema> LedStream<T> allocate(InputStream inputStream, SchemaFactory<T> factory, int schemaSize) {
		return new InputLedStream<>(inputStream, factory, schemaSize);
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

	private <T extends Schema> IndexedAllocatorFactory<T> getDefaultAllocatorFactory() {
		return (inputStream, elementCount, elementSize, factory) -> {
			if(elementCount >= 0 && elementCount * elementSize < Integer.MAX_VALUE) {
				return IndexedAllocatorFactory.newManagedIndexedAllocator(inputStream, elementCount, elementSize, factory);
			}
			return IndexedAllocatorFactory.newArrayAllocator(inputStream, elementCount, elementSize, factory);
		};
	}

}