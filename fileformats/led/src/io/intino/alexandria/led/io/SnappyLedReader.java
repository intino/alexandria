package io.intino.alexandria.led.io;

import io.intino.alexandria.led.ArrayLed;
import io.intino.alexandria.led.IndexedLed;
import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.allocators.indexed.ArrayAllocator;
import io.intino.alexandria.logger.Logger;
import org.xerial.snappy.SnappyInputStream;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static io.intino.alexandria.led.util.MemoryUtils.allocBuffer;
import static io.intino.alexandria.led.util.MemoryUtils.memcpy;

public class SnappyLedReader implements LedReader {
	private final InputStream is;

	public SnappyLedReader(File file) {
		this(inputStreamOf(file));
	}

	public SnappyLedReader(InputStream is) {
		this.is = is;
	}

	@Override
	public <S extends Schema> IndexedLed<S> read(SchemaFactory<S> factory) {
		IndexedLed<S> led = null;
		byte[] size = new byte[4];
		try (InputStream stream = is) {
			stream.read(size);
			int elementSize = ByteBuffer.wrap(size).getInt();
			try (SnappyInputStream inputStream = new SnappyInputStream(is)) {
				List<ByteBuffer> buffers = new ArrayList<>();
				byte[] inputBuffer = new byte[1024 * elementSize];
				int bytesRead;
				while ((bytesRead = inputStream.read(inputBuffer)) > 0) {
					ByteBuffer buffer = allocBuffer(bytesRead);
					memcpy(inputBuffer, 0, buffer, 0, bytesRead);
					buffers.add(buffer);
				}
				led = new ArrayLed<>(new ArrayAllocator<>(buffers, elementSize, factory));
			} catch (IOException e) {
				Logger.error(e);
			}
		} catch (IOException e) {
			Logger.error(e);
		}
		return led;
	}

	private static InputStream inputStreamOf(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			return new ByteArrayInputStream(new byte[0]);
		}
	}
}
