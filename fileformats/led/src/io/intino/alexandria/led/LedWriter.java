package io.intino.alexandria.led;

import io.intino.alexandria.logger.Logger;
import org.xerial.snappy.SnappyOutputStream;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.intino.alexandria.led.util.memory.MemoryUtils.*;
import static java.nio.file.StandardOpenOption.WRITE;

public class LedWriter {

	private static final int DEFAULT_BUFFER_SIZE = 4096;


	private int bufferSize = DEFAULT_BUFFER_SIZE;
	private final OutputStream destOutputStream;
	private final File destinationFile;

	public LedWriter(File destOutputStream) {
		destOutputStream.getAbsoluteFile().getParentFile().mkdirs();
		this.destinationFile = destOutputStream;
		this.destOutputStream = outputStream(destOutputStream);
	}

	public LedWriter(OutputStream destOutputStream) {
		this.destOutputStream = destOutputStream;
		destinationFile = null;
	}

	public int bufferSize() {
		return bufferSize;
	}

	public LedWriter bufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
		return this;
	}

	private FileOutputStream outputStream(File destination) {
		try {
			return new FileOutputStream(destination);
		} catch (FileNotFoundException e) {
			Logger.error(e);
			return null;
		}
	}

	public void write(Led<? extends Schema> led) {
		serialize(led);
	}

	public void write(LedStream<? extends Schema> led) {
		serialize(led);
	}

	public <T extends Schema> void writeUncompressed(LedStream<T> ledStream) {
		if(destinationFile != null) {
			fastSerializeUncompressed(ledStream);
		} else {
			serializeUncompressed(ledStream);
		}
	}

	private <T extends Schema> void fastSerializeUncompressed(LedStream<T> ledStream) {
		try (FileChannel fileChannel = FileChannel.open(destinationFile.toPath(), WRITE)) {
			final int schemaSize = ledStream.schemaSize();
			ByteBuffer outputBuffer = allocBuffer((long) bufferSize * schemaSize);
			final long destPtr = addressOf(outputBuffer);
			int offset = 0;
			while (ledStream.hasNext()) {
				Schema schema = ledStream.next();
				memcpy(schema.address() + schema.baseOffset(), destPtr + offset, schemaSize);
				offset += schemaSize;
				if (offset == outputBuffer.capacity()) {
					fileChannel.write(outputBuffer);
					outputBuffer.clear();
					offset = 0;
				}
			}
			if (offset > 0) {
				outputBuffer.limit(offset);
				fileChannel.write(outputBuffer);
				outputBuffer.clear();
			}
			destOutputStream.close();
			ledStream.close();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	private <T extends Schema> void serializeUncompressed(LedStream<T> ledStream) {
		try (OutputStream outputStream = this.destOutputStream) {
			final int schemaSize = ledStream.schemaSize();
			final byte[] outputBuffer = new byte[bufferSize * schemaSize];
			int offset = 0;
			while (ledStream.hasNext()) {
				Schema schema = ledStream.next();
				memcpy(schema.address(), schema.baseOffset(), outputBuffer, offset, schemaSize);
				offset += schemaSize;
				if (offset == outputBuffer.length) {
					writeToOutputStream(outputStream, outputBuffer);
					offset = 0;
				}
			}
			if (offset > 0) {
				writeToOutputStream(outputStream, outputBuffer, 0, offset);
			}
			ledStream.close();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	private void serialize(Led<? extends Schema> led) {
		if (led.size() == 0) return;
		ExecutorService executor = Executors.newSingleThreadExecutor();
		final long size = led.size();
		final int schemaSize = led.schemaSize();
		final int numBatches = (int) Math.ceil(led.size() / (float) bufferSize);
		try (OutputStream fos = this.destOutputStream) {
			LedHeader header = new LedHeader();
			header.elementCount(size).elementSize(schemaSize);
			fos.write(header.toByteArray());
			try (SnappyOutputStream outputStream = new SnappyOutputStream(fos)) {
				for (int i = 0; i < numBatches; i++) {
					final int start = i * bufferSize;
					final int numElements = (int) Math.min(bufferSize, led.size() - start);
					byte[] outputBuffer = new byte[numElements * schemaSize];
					for (int j = 0; j < numElements; j++) {
						Schema src = led.schema(j + start);
						final long offset = j * schemaSize;
						memcpy(src.address(), src.baseOffset(), outputBuffer, offset, schemaSize);
					}
					executor.submit(() -> writeToOutputStream(outputStream, outputBuffer));
				}
				executor.shutdown();
				executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	private void serialize(LedStream<? extends Schema> ledStream) {
		long elementCount = 0;
		try (OutputStream fos = this.destOutputStream) {
			LedHeader header = new LedHeader();
			header.elementCount(LedHeader.UNKNOWN_SIZE).elementSize(ledStream.schemaSize());
			fos.write(header.toByteArray());
			try (SnappyOutputStream outputStream = new SnappyOutputStream(fos)) {
				final int schemaSize = ledStream.schemaSize();
				final byte[] outputBuffer = new byte[bufferSize * schemaSize];
				int offset = 0;
				while (ledStream.hasNext()) {
					Schema schema = ledStream.next();
					memcpy(schema.address(), schema.baseOffset(), outputBuffer, offset, schemaSize);
					offset += schemaSize;
					if (offset == outputBuffer.length) {
						writeToOutputStream(outputStream, outputBuffer);
						offset = 0;
					}
					++elementCount;
				}
				if (offset > 0) {
					writeToOutputStream(outputStream, outputBuffer, 0, offset);
				}
			}
			ledStream.close();
		} catch (Exception e) {
			Logger.error(e);
		}
		if(destinationFile != null) {
			overrideHeader(elementCount, ledStream.schemaSize());
		}
	}

	private void overrideHeader(long elementCount, int schemaSize) {
		try(RandomAccessFile raFile = new RandomAccessFile(destinationFile, "rw")) {
			raFile.writeLong(elementCount);
			raFile.writeInt(schemaSize);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void writeToOutputStream(OutputStream outputStream, byte[] outputBuffer) {
		writeToOutputStream(outputStream, outputBuffer, 0, outputBuffer.length);
	}

	private void writeToOutputStream(OutputStream outputStream, byte[] outputBuffer, int offset, int size) {
		try {
			outputStream.write(outputBuffer, offset, size);
		} catch (IOException e) {
			Logger.error(e);
		}
	}
}
