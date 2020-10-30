package io.intino.alexandria.led;

import io.intino.alexandria.logger.Logger;
import org.xerial.snappy.SnappyOutputStream;

import java.io.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.intino.alexandria.led.util.MemoryUtils.memcpy;

public class LedWriter {

	private final int bufferSize = 1024;
	private final OutputStream destination;
	private final File destinationFile;

	public LedWriter(File destination) {
		destination.getAbsoluteFile().getParentFile().mkdirs();
		this.destinationFile = destination;
		this.destination = outputStream(destination);
	}

	public LedWriter(OutputStream destination) {
		this.destination = destination;
		destinationFile = null;
	}

	public FileOutputStream outputStream(File destination) {
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
		serialize(led);
	}

	private void serialize(Led<? extends Transaction> led) {
		if (led.size() == 0) return;
		ExecutorService executor = Executors.newSingleThreadExecutor();
		final long size = led.size();
		final int transactionSize = led.transactionSize();
		final int numBatches = (int) Math.ceil(led.size() / (float) bufferSize);
		List<? extends Transaction> elements = led.elements();
		try (OutputStream fos = this.destination) {
			LedHeader header = new LedHeader();
			header.elementCount(size).elementSize(transactionSize);
			fos.write(header.toByteArray());
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

	private void serialize(LedStream<? extends Transaction> ledStream) {
		long elementCount = 0;
		try (OutputStream fos = this.destination) {
			LedHeader header = new LedHeader();
			header.elementCount(LedHeader.UNKNOWN_SIZE).elementSize(ledStream.transactionSize());
			fos.write(header.toByteArray());
			try (SnappyOutputStream outputStream = new SnappyOutputStream(fos)) {
				final int transactionSize = ledStream.transactionSize();
				final byte[] outputBuffer = new byte[bufferSize * transactionSize];
				int offset = 0;
				while (ledStream.hasNext()) {
					Transaction schema = ledStream.next();
					memcpy(schema.address(), schema.baseOffset(), outputBuffer, offset, transactionSize);
					offset += transactionSize;
					if (offset == outputBuffer.length) {
						writeToOutputStream(outputStream, outputBuffer);
						offset = 0;
					}
					++elementCount;
				}
				if (offset > 0) {
					writeToOutputStream(outputStream, outputBuffer, 0, offset);
				}
			} catch (IOException e) {
				Logger.error(e);
			}
		} catch (IOException e) {
			Logger.error(e);
		}
		if(destinationFile != null) {
			overrideHeader(elementCount, ledStream.transactionSize());
		}
	}

	private void overrideHeader(long elementCount, int transactionSize) {
		try(RandomAccessFile raFile = new RandomAccessFile(destinationFile, "rw")) {
			raFile.writeLong(elementCount);
			raFile.writeInt(transactionSize);
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
