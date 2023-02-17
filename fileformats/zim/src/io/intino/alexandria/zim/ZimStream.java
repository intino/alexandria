package io.intino.alexandria.zim;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageReader;
import org.xerial.snappy.SnappyInputStream;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@SuppressWarnings({"all"})
public class ZimStream extends AbstractZimStream implements Iterator<Message>, AutoCloseable {
	public static ZimStream sequence(File first, File... rest) throws IOException {
		ZimStream[] streams = new ZimStream[1 + rest.length];
		streams[0] = ZimStream.of(first);
		for(int i = 0;i < rest.length;i++) streams[i + 1] = ZimStream.of(rest[i]);
		return new ZimStream(Arrays.stream(streams).flatMap(Function.identity()).iterator());
	}

	public static ZimStream sequence(Stream<Message>... streams) {
		return new ZimStream(Arrays.stream(streams).flatMap(Function.identity()).iterator());
	}

	public static ZimStream of(InputStream is) {
		return new ZimStream(readerOf(is instanceof SnappyInputStream ? is : ZimStream.snZipStreamOf(is)));
	}

	public static ZimStream of(String text) {
		return ZimStream.of(new MessageReader(text));
	}

	public static ZimStream of(Message... messages) {
		return new ZimStream(Arrays.stream(messages).iterator());
	}

	public static ZimStream of(Collection<Message> messages) {
		return new ZimStream(messages.iterator());
	}

	public static ZimStream of(Stream<Message> messages) {
		return new ZimStream(messages.iterator());
	}

	public static ZimStream of(MessageReader reader) {
		return new ZimStream(reader.iterator());
	}

	private final Iterator<Message> iterator;
	private final List<Runnable> closeHandlers;

	public static ZimStream of(File file) throws IOException {
		return new ZimStream(readerOf(inputStream(file)));
	}

	public ZimStream(Iterator<Message> iterator) {
		this.iterator = requireNonNull(iterator);
		this.closeHandlers = new LinkedList<>();
	}

	@Override
	public Iterator<Message> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Message next() {
		return iterator.next();
	}

	@Override
	public void forEach(Consumer<? super Message> action) {
		try {
			while(hasNext()) {
				action.accept(next());
			}
		} finally {
			close();
		}
	}

	@Override
	public Stream<Message> onClose(Runnable closeHandler) {
		if(closeHandler != null) this.closeHandlers.add(closeHandler);
		return this;
	}

	@Override
	public void close() {
		closeIterator(iterator);
		closeHandlers.forEach(Runnable::run);
	}

	private static void closeIterator(Iterator<Message> iterator) {
		if(iterator instanceof AutoCloseable) {
			try {
				((AutoCloseable) iterator).close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static MessageReader readerOf(InputStream is) {
		return new MessageReader(is);
	}

	private static InputStream inputStream(File file) throws IOException {
		return snZipStreamOf(file);
	}

	private static InputStream snZipStreamOf(File file) throws IOException {
		return new SnappyInputStream(fileInputStream(file));
	}

	private static InputStream snZipStreamOf(InputStream stream) {
		try {
			return new SnappyInputStream(stream);
		} catch (IOException e) {
			Logger.error(e);
			return stream;
		}
	}

	private static BufferedInputStream fileInputStream(File file) throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(file));
	}
}