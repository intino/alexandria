package io.intino.alexandria.zim;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageReader;
import org.xerial.snappy.SnappyInputStream;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ZimReader implements ZimStream {
	public static final String ZimExtension = ".zim";
	private final Iterator<Message> iterator;
	private Message current;

	public ZimReader(File file) {
		this(iteratorOf(inputStream(file)));
	}

	public ZimReader(InputStream is) {
		this(iteratorOf(is instanceof SnappyInputStream ? is : ZimReader.snZipStreamOf(is)));
	}

	public ZimReader(String text) {
		this(iteratorOf(new ByteArrayInputStream(text.getBytes())));
	}

	public ZimReader(Message... messages) {
		this(stream(messages));
	}

	public ZimReader(List<Message> messages) {
		this(messages.stream());
	}

	public ZimReader(Stream<Message> stream) {
		this(stream.iterator());
	}

	public ZimReader(Iterator<Message> iterator) {
		this.iterator = iterator;
	}

	public Iterator<Message> iterator() {
		return iterator;
	}

	@Override
	public Message current() {
		return current;
	}

	@Override
	public Message next() {
		return current = iterator.hasNext() ? iterator.next() : null;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	private static Iterator<Message> iteratorOf(InputStream is) {
		MessageReader source = readerOf(is);
		return new Iterator<>() {
			private Message current;
			private Message next;

			@Override
			public Message next() {
				if (current == next) hasNext();
				current = next;
				return current;
			}

			private Message nextMessageFromSource() {
				return source.next();
			}

			@Override
			public boolean hasNext() {
				if (current != next) return true;
				next = nextMessageFromSource();
				boolean hasNext = next != null;
				if (!hasNext) source.close();
				return hasNext;
			}

		};
	}

	private static MessageReader readerOf(InputStream is) {
		return new MessageReader(is);
	}

	private static InputStream inputStream(File file) {
		try {
			return snZipStreamOf(file);
		} catch (IOException e) {
			return new ByteArrayInputStream(new byte[0]);
		}
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
