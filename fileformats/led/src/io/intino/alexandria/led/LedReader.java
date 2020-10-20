package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.allocators.stack.StackAllocator;
import io.intino.alexandria.led.allocators.stack.StackAllocators;
import io.intino.alexandria.logger.Logger;
import org.xerial.snappy.SnappyInputStream;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

	public <S extends Transaction> LedStream<S> read(TransactionFactory<S> factory) {
		try {
			ByteBuffer header = ByteBuffer.wrap(is.readNBytes(8));
			int ledSize = header.getInt();
			long transactionSize = header.getInt();
			return allocate(new SnappyInputStream(is), factory, (int) transactionSize);
		} catch (IOException e) {
			Logger.error(e);
		}
		return null;
	}

	private <S extends Transaction> LedStream<S> allocate(SnappyInputStream inputStream, TransactionFactory<S> factory, int transactionSize) {
		return new ReaderLedStream<>(inputStream, factory, transactionSize);
	}

	private static InputStream inputStreamOf(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			return new ByteArrayInputStream(new byte[0]);
		}
	}

	private static class ReaderLedStream<T extends Transaction> implements LedStream<T> {
		private static final int INPUT_BUFFER_MIN_SIZE = 1024;

		private final InputStream inputStream;
		private final int transactionSize;
		private final TransactionFactory<T> provider;
		private final Iterator<T> iterator;

		public ReaderLedStream(InputStream inputStream, TransactionFactory<T> provider, int transactionSize) {
			this.inputStream = inputStream;
			this.transactionSize = transactionSize;
			this.provider = provider;
			this.iterator = stream().iterator();
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public T next() {
			return iterator.next();
		}

		@SuppressWarnings("unchecked")
		public synchronized Stream<T> stream() {
			return Stream.generate(() -> read(inputStream))
					.takeWhile(inputBuffer -> checkInputBuffer(inputBuffer, inputStream))
					.flatMap(this::allocateAll);
		}

		@Override
		public int transactionSize() {
			return transactionSize;
		}

		private boolean checkInputBuffer(ByteBuffer inputBuffer, InputStream inputStream) {
			if (inputBuffer != null) return true;
			closeInputStream(inputStream);
			return false;
		}

		private synchronized void closeInputStream(InputStream inputStream) {
			try {
				inputStream.close();
			} catch (IOException e) {
				Logger.error(e);
			}
		}

		private Stream<T> allocateAll(ByteBuffer bytes) {
			StackAllocator<T> allocator = StackAllocators.newManaged(transactionSize, bytes, provider);
			return IntStream.range(0, bytes.remaining() / transactionSize)
					.sorted()
					.parallel()
					.mapToObj(index -> allocator.malloc());
		}

		private ByteBuffer read(InputStream inputStream) {
			try {
				if (inputStream == null || inputStream.available() <= 0) return null;
				byte[] inputBuffer = new byte[INPUT_BUFFER_MIN_SIZE * transactionSize];
				int bytesRead;
				bytesRead = inputStream.read(inputBuffer);
				if (bytesRead < 0) return null;
				ByteBuffer buffer = allocBuffer(bytesRead);
				buffer.put(inputBuffer, 0, bytesRead);
				buffer.clear();
				return buffer;
			} catch (Exception e) {
				Logger.error(e);
			}
			return null;
		}

		@Override
		public void close() throws Exception {
			inputStream.close();
		}

	}
}