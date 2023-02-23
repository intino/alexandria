package io.intino.alexandria.zim;

import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageReader;
import io.intino.alexandria.resourcecleaner.DisposableResource;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@SuppressWarnings({"all"})
public class ZimStream extends AbstractZimStream implements Iterator<Message>, AutoCloseable {

	public static ZimStream sequence(File first, File... rest) throws IOException {
		ZimStream[] streams = new ZimStream[1 + rest.length];
		streams[0] = ZimStream.of(first);
		for (int i = 0; i < rest.length; i++) streams[i + 1] = ZimStream.of(rest[i]);
		return new ZimStream(Arrays.stream(streams).flatMap(Function.identity()).iterator());
	}

	public static ZimStream sequence(Stream<Message>... streams) {
		return new ZimStream(Arrays.stream(streams).flatMap(Function.identity()).iterator());
	}

	public static ZimStream of(File file) throws IOException {
		return new ZimStream(!file.exists() ? Collections.emptyIterator() : readerOf(Zim.decompressing(fileInputStream(file))));
	}

	public static ZimStream of(InputStream is) throws IOException {
		return new ZimStream(readerOf(Zim.decompressing(is)));
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
	private final DisposableResource resource;

	public ZimStream(Iterator<Message> iterator) {
		this.iterator = requireNonNull(iterator);
		this.resource = DisposableResource.whenDestroyed(this).thenClose(iterator);
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
			while (hasNext()) {
				action.accept(next());
			}
		} finally {
			close();
		}
	}

	@Override
	public Stream<Message> onClose(Runnable closeHandler) {
		if (closeHandler != null) resource.addCloseHandler(closeHandler);
		return this;
	}

	@Override
	public void close() {
		resource.close();
	}

	private static MessageReader readerOf(InputStream is) {
		return new MessageReader(is);
	}

	private static BufferedInputStream fileInputStream(File file) throws IOException {
		return new BufferedInputStream(new FileInputStream(file));
	}
}