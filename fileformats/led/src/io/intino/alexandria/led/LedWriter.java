package io.intino.alexandria.led;

import io.intino.alexandria.led.leds.ListLed;
import io.intino.alexandria.logger.Logger;
import org.xerial.snappy.SnappyOutputStream;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

	public void write(Led<? extends Transaction> led) {
		serialize(led);
	}

	public void write(LedStream<? extends Transaction> led) {
		Iterable<? extends Transaction> iterable = () -> (Iterator<Transaction>) led;
		serialize(new ListLed<>(StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList())));
	}

	private void serialize(Led<? extends Transaction> led) {
		if (led.size() == 0) return;
		ExecutorService executor = Executors.newSingleThreadExecutor();
		final long size = led.size();
		final int transactionSize = led.transactionSize();
		final int numBatches = (int) Math.ceil(led.size() / (float) bufferSize);
		List<? extends Transaction> elements = led.elements();
		try (OutputStream fos = this.destination) {
			fos.write(ByteBuffer.allocate(4).putInt((int) size).array());
			fos.write(ByteBuffer.allocate(4).putInt(transactionSize).array());
			try (SnappyOutputStream outputStream = new SnappyOutputStream(fos)) {
				for (int i = 0; i < numBatches; i++) {
					final int start = i * bufferSize;
					final int numElements = (int) Math.min(bufferSize, led.size() - start);
					byte[] outputBuffer = new byte[numElements * transactionSize];
					for (int j = 0; j < numElements; j++) {
						Transaction src = elements.get(j + start);
						final long offset = j * transactionSize;
						memcpy(src.address(), src.baseOffset(), outputBuffer, offset, transactionSize);
					}
					executor.submit(() -> writeToOutputStream(outputStream, outputBuffer));
				}
				executor.shutdown();
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			} catch (IOException | InterruptedException e) {
				Logger.error(e);
			}
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void serialize(LedStream<? extends Transaction> led) {
		try (OutputStream fos = this.destination) {
			fos.write(ByteBuffer.allocate(4).putInt(0).array()); //TODO
			fos.write(ByteBuffer.allocate(4).putInt(led.transactionSize()).array());
			try (SnappyOutputStream outputStream = new SnappyOutputStream(fos)) {
				final int transactionSize = led.transactionSize();
				final byte[] outputBuffer = new byte[bufferSize * transactionSize];
				final ExecutorService writeToOutputStreamThread = Executors.newSingleThreadExecutor();
				int offset = 0;
				while (led.hasNext()) {
					Transaction schema = led.next();
					memcpy(schema.address(), schema.baseOffset(), outputBuffer, offset, transactionSize);
					offset += transactionSize;
					if (offset == outputBuffer.length) {
						writeToOutputStreamThread.submit(() -> writeToOutputStream(outputStream, outputBuffer));
						offset = 0;
					}
				}
				if (offset > 0) {
					final int size = offset;
					writeToOutputStreamThread.submit(() -> writeToOutputStream(outputStream, outputBuffer, 0, size));
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
