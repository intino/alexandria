package io.intino.alexandria.led.io;

import io.intino.alexandria.led.IndexedLed;
import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.Schema;
import io.intino.alexandria.logger.Logger;
import org.xerial.snappy.SnappyOutputStream;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.intino.alexandria.led.util.MemoryUtils.memcpy;

public class LedWriter {
	private final int bufferSize = 1024;
	private final OutputStream destination;

	public LedWriter(File destination) {
		destination.getAbsoluteFile().getParentFile().mkdirs();
		this.destination = outputStream(destination);
	}

	public LedWriter(OutputStream destination) {
		this.destination = destination;
	}

	public OutputStream outputStream(File destination) {
		try {
			return new FileOutputStream(destination);
		} catch (FileNotFoundException e) {
			Logger.error(e);
			return null;
		}
	}


	public void write(Led<? extends Schema> led) {
		if (led instanceof IndexedLed) serialize((IndexedLed<? extends Schema>) led);
		else serialize(led);
	}

	private void serialize(IndexedLed<? extends Schema> led) {
		if (led.size() == 0) return;
		ExecutorService writeToOutputStreamThread = Executors.newSingleThreadExecutor();
		final long size = led.size();
		final int elementSize = led.schemaSize();
		final int numBatches = (int) Math.ceil(led.size() / (float) bufferSize);
		try (OutputStream fos = this.destination) {
			fos.write(ByteBuffer.allocate(4).putInt((int) size).array());
			fos.write(ByteBuffer.allocate(4).putInt(elementSize).array());
			try (SnappyOutputStream outputStream = new SnappyOutputStream(fos)) {
				for (int i = 0; i < numBatches; i++) {
					final int start = i * bufferSize;
					final int numElements = (int) Math.min(bufferSize, led.size() - start);
					byte[] outputBuffer = new byte[numElements * elementSize];
					for (int j = 0; j < numElements; j++) {
						Schema src = led.get(j + start);
						final long offset = j * elementSize;
						memcpy(src.address(), src.baseOffset(), outputBuffer, offset, elementSize);
					}
					writeToOutputStreamThread.submit(() -> writeToOutputStream(outputStream, outputBuffer));
				}
				writeToOutputStreamThread.shutdown();
				writeToOutputStreamThread.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			} catch (IOException | InterruptedException e) {
				Logger.error(e);
			}
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void serialize(Led<? extends Schema> led) {
		try (OutputStream fos = this.destination) {
			fos.write(led.schemaSize());
			try (SnappyOutputStream outputStream = new SnappyOutputStream(fos)) {
				final int elementSize = led.schemaSize();
				final byte[] outputBuffer = new byte[bufferSize * elementSize];
				final Iterator<? extends Schema> iterator = led.stream().iterator();
				final ExecutorService writeToOutputStreamThread = Executors.newSingleThreadExecutor();
				int offset = 0;
				while (iterator.hasNext()) {
					Schema schema = iterator.next();
					memcpy(schema.address(), schema.baseOffset(), outputBuffer, offset, elementSize);
					offset += elementSize;
					if (offset == outputBuffer.length) {
						writeToOutputStreamThread.submit(() -> writeToOutputStream(outputStream, outputBuffer));
						offset = 0;
					}
				}
				if (offset > 0) {
					final int size = offset;
					writeToOutputStreamThread.submit(() -> writeToOutputStream(outputStream, outputBuffer, 0, size));
					outputStream.write(outputBuffer, 0, offset);
				}
				writeToOutputStreamThread.shutdown();
				writeToOutputStreamThread.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			} catch (IOException | InterruptedException e) {
				Logger.error(e);
			}
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void writeToOutputStream(SnappyOutputStream outputStream, byte[] outputBuffer) {
		writeToOutputStream(outputStream, outputBuffer, 0, outputBuffer.length);
	}

	private void writeToOutputStream(SnappyOutputStream outputStream, byte[] outputBuffer, int offset, int size) {
		try {
			outputStream.write(outputBuffer, offset, size);
		} catch (IOException e) {
			Logger.error(e);
		}
	}
}
