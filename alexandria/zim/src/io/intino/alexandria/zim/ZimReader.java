package io.intino.alexandria.zim;

import io.intino.alexandria.inl.InlReader;
import io.intino.alexandria.inl.Message;
import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ZimReader implements ZimStream {
	public static final String ZimExtension = ".zim";
	private final Iterator<Message> iterator;
	private Message current;

	public ZimReader(File file) {
		this(iteratorOf(inputStream(file)));
	}

	public ZimReader(InputStream is) {
		this(iteratorOf(is));
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
		this(stream.sorted(comparing(m -> m.asEvent().instant())).iterator());
	}

	public ZimReader(Iterator<Message> iterator) {
		this.iterator = iterator;
	}

	private static Iterator<Message> iteratorOf(InputStream is) {
		InlReader source = readerOf(is);
		return new Iterator<Message>() {
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
				if (!hasNext) close();
				return hasNext;
			}

			void close() {
				try {
					source.close();
				} catch (IOException e) {
					Logger.error(e);
				}
			}

		};
	}

	private static InlReader readerOf(InputStream is) {
		return new InlReader(is);
	}

	private static InputStream inputStream(File file) {
		try {
			return zipStreamOf(file);
		} catch (IOException e) {
			return new ByteArrayInputStream(new byte[0]);
		}
	}

	private static InputStream zipStreamOf(File file) throws IOException {
		return new GZIPInputStream(new BufferedInputStream(new FileInputStream(file)));
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

}
