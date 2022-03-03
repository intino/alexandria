package io.intino.alexandria.led.legacy;

import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.SchemaSerialUUIDMismatchException;
import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocator;
import io.intino.alexandria.led.allocators.indexed.IndexedAllocatorFactory;
import io.intino.alexandria.led.leds.ByteChannelLedStream;
import io.intino.alexandria.led.leds.IndexedLed;
import io.intino.alexandria.led.leds.InputLedStream;
import io.intino.alexandria.logger.Logger;
import org.xerial.snappy.SnappyInputStream;

import java.io.*;
import java.util.Objects;
import java.util.UUID;

public class LegacyLedReader {

	private final InputStream srcInputStream;
	private final File sourceFile;

	public LegacyLedReader(File file) {
		this.srcInputStream = inputStreamOf(file);
		this.sourceFile = file;
	}

	public LegacyLedReader(InputStream srcInputStream) {
		this.srcInputStream = srcInputStream;
		this.sourceFile = null;
	}

	public int size() {
		if(sourceFile == null) return (int) LegacyLedHeader.UNKNOWN_SIZE;
		try(RandomAccessFile raFile = new RandomAccessFile(sourceFile, "r")) {
			return (int) raFile.readLong();
		} catch (IOException e) {
			Logger.error(e);
		}
		return (int) LegacyLedHeader.UNKNOWN_SIZE;
	}

	public <T extends Schema> Led<T> readAll(Class<T> schemaClass) {
		return readAll(getDefaultAllocatorFactory(), schemaClass);
	}

	public <T extends Schema> Led<T> readAll(IndexedAllocatorFactory<T> allocatorFactory, Class<T> schemaClass) {
		try {
			if(srcInputStream.available() == 0) {
				return Led.empty(schemaClass);
			}
		} catch(Exception e) {
			Logger.error(e);
			return Led.empty(schemaClass);
		}
		LegacyLedHeader header = LegacyLedHeader.from(this.srcInputStream);
		return readAllIntoMemory(allocatorFactory, schemaClass, header);
	}

	private <T extends Schema> Led<T> readAllIntoMemory(IndexedAllocatorFactory<T> allocatorFactory, Class<T> schemaClass,
														LegacyLedHeader header) {
		try(SnappyInputStream inputStream = new SnappyInputStream(this.srcInputStream)) {
			IndexedAllocator<T> allocator = allocatorFactory.create(inputStream, header.elementCount(), header.elementSize(), schemaClass);
			return new IndexedLed<>(allocator);
		} catch (IOException e) {
			Logger.error(e);
		}
		return Led.empty(schemaClass);
	}

	public <T extends Schema> LedStream<T> read(Class<T> schemaClass) {
		try {
			if(srcInputStream.available() == 0) return LedStream.empty(schemaClass);
			LegacyLedHeader header = LegacyLedHeader.from(srcInputStream);
			return readAsStream(new SnappyInputStream(srcInputStream), schemaClass, header.elementSize());
		} catch (IOException e) {
			Logger.error(e);
		}
		return LedStream.empty(schemaClass);
	}

	public <T extends Schema> LedStream<T> readUncompressed(int elementSize, Class<T> schemaClass) {
		try {
			if(srcInputStream.available() == 0) return LedStream.empty(schemaClass);
			return allocateUncompressed(Schema.factoryOf(schemaClass), elementSize);
		} catch (IOException e) {
			Logger.error(e);
		}
		return LedStream.empty(schemaClass);
	}

	private <T extends Schema> LedStream<T> allocateUncompressed(SchemaFactory<T> factory, int elementSize) {
		try {
			srcInputStream.close();
		} catch (IOException e) {
			Logger.error(e);
		}
		return new ByteChannelLedStream<>(sourceFile, factory, elementSize);
	}

	private <T extends Schema> LedStream<T> readAsStream(InputStream inputStream, Class<T> schemaClass, int schemaSize) {
		return new InputLedStream<>(inputStream, Schema.factoryOf(schemaClass), schemaSize);
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

	private void checkSerialUUID(UUID srcUUID, UUID dstUUID) {
		if(!Objects.equals(srcUUID, dstUUID)) {
			throw new SchemaSerialUUIDMismatchException(srcUUID, dstUUID);
		}
	}

	private <T extends Schema> IndexedAllocatorFactory<T> getDefaultAllocatorFactory() {
		return (inputStream, elementCount, elementSize, schemaClass) -> {
			if(elementCount >= 0 && elementCount * elementSize < Integer.MAX_VALUE) {
				return IndexedAllocatorFactory.newManagedIndexedAllocator(inputStream, elementCount, elementSize, schemaClass);
			}
			return IndexedAllocatorFactory.newArrayAllocator(inputStream, elementCount, elementSize, schemaClass);
		};
	}

}