package io.intino.alexandria.led.io;

import io.intino.alexandria.led.ArrayLed;
import io.intino.alexandria.led.IndexedLed;
import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.allocators.indexed.ArrayAllocator;
import io.intino.alexandria.led.allocators.indexed.ManagedIndexedAllocator;
import io.intino.alexandria.logger.Logger;
import org.xerial.snappy.SnappyInputStream;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static io.intino.alexandria.led.util.MemoryUtils.allocBuffer;
import static io.intino.alexandria.led.util.MemoryUtils.memcpy;

public class LedReader {
	private final InputStream is;

	public LedReader(File file) {
		this(inputStreamOf(file));
	}

	public LedReader(InputStream is) {
		this.is = is;
	}

	public int size() {
		try (InputStream stream = is) {
			int anInt = ByteBuffer.wrap(stream.readNBytes(4)).getInt();
			is.reset();
			return anInt;
		} catch (IOException e) {
			Logger.error(e);
			return 0;
		}
	}

	public <S extends Schema> IndexedLed<S> read(SchemaFactory<S> factory) {
		try (InputStream stream = is) {
			int ledSize = ByteBuffer.wrap(stream.readNBytes(4)).getInt();
			int schemaSize = ByteBuffer.wrap(stream.readNBytes(4)).getInt();
			try (SnappyInputStream inputStream = new SnappyInputStream(is)) {
				return ledSize < 100 ?
						tryWithArrayAllocator(inputStream, factory, schemaSize) :
						tryWithManagedAllocator(inputStream, factory, ledSize, schemaSize);
			} catch (IOException e) {
				Logger.error(e);
			}
		} catch (IOException e) {
			Logger.error(e);
		}
		return null;
	}

	private <S extends Schema> IndexedLed<S> tryWithManagedAllocator(SnappyInputStream inputStream, SchemaFactory<S> factory, int ledSize, int schemaSize) throws IOException {
		ByteBuffer buffer = allocBuffer(ledSize * schemaSize).put(inputStream.readAllBytes());
		return new ArrayLed<>(new ManagedIndexedAllocator<>(buffer, 0, ledSize, schemaSize, factory));
	}

	private <S extends Schema> IndexedLed<S> tryWithArrayAllocator(SnappyInputStream inputStream, SchemaFactory<S> factory, int schemaSize) throws IOException {
		List<ByteBuffer> buffers = new ArrayList<>();
		byte[] inputBuffer = new byte[1024 * schemaSize];
		int bytesRead;
		while ((bytesRead = inputStream.read(inputBuffer)) > 0) {
			ByteBuffer buffer = allocBuffer(bytesRead);
			memcpy(inputBuffer, 0, buffer, 0, bytesRead);
			buffers.add(buffer);
		}
		return new ArrayLed<>(new ArrayAllocator<>(buffers, schemaSize, factory));
	}

	private static InputStream inputStreamOf(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			return new ByteArrayInputStream(new byte[0]);
		}
	}
}