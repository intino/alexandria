package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.allocators.stack.StackAllocator;
import io.intino.alexandria.led.allocators.stack.StackAllocators;
import io.intino.alexandria.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.intino.alexandria.led.util.MemoryUtils.allocBuffer;

public class InputStreamLed<T extends Schema> implements Led<T> {

	private static final int INPUT_BUFFER_MIN_SIZE = 1024;


	private final InputStreamSupplier inputStreamSupplier;
	private final Set<InputStream> openedInputStreams;
	private final int elementSize;
	private final SchemaFactory<T> provider;

	public InputStreamLed(int elementSize, InputStreamSupplier inputStreamSupplier, SchemaFactory<T> provider) {
		this.elementSize = elementSize;
		this.inputStreamSupplier = inputStreamSupplier;
		this.provider = provider;
		openedInputStreams = new HashSet<>();
	}

	@Override
	public int elementSize() {
		return elementSize;
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized Stream<T> stream() {
		InputStream inputStream = inputStreamSupplier.get();
		openedInputStreams.add(inputStream);
		return Stream.generate(() -> read(inputStream))
				.takeWhile(inputBuffer -> checkInputBuffer(inputBuffer, inputStream))
				.flatMap(this::allocateAll);
	}

	private boolean checkInputBuffer(ByteBuffer inputBuffer, InputStream inputStream) {

		if (inputBuffer != null) {
			return true;
		}

		closeInputStream(inputStream);

		return false;
	}

	private synchronized void closeInputStream(InputStream inputStream) {
		try {
			inputStream.close();
			openedInputStreams.remove(inputStream);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private Stream<T> allocateAll(ByteBuffer bytes) {
		StackAllocator<T> allocator = StackAllocators.newManaged(elementSize, bytes, provider);
		return IntStream.range(0, bytes.remaining() / elementSize)
				.sorted()
				.parallel()
				.mapToObj(index -> allocator.malloc());
	}

	private ByteBuffer read(InputStream inputStream) {
		try {
			if (inputStream == null || inputStream.available() <= 0) return null;
			byte[] inputBuffer = new byte[INPUT_BUFFER_MIN_SIZE * elementSize];
			int bytesRead = -1;
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
		for (InputStream openedInputStream : openedInputStreams) openedInputStream.close();
	}

	public interface InputStreamSupplier extends Supplier<InputStream> {

		@Override
		default InputStream get() {
			try {
				return newInputStream();
			} catch (IOException e) {
				Logger.error(e);
			}
			return null;
		}

		InputStream newInputStream() throws IOException;
	}
}
