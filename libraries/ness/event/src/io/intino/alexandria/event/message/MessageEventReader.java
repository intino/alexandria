package io.intino.alexandria.event.message;

import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageReader;
import io.intino.alexandria.zim.ZimReader;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class MessageEventReader implements EventReader<MessageEvent> {

	private final Iterator<MessageEvent> iterator;

	public MessageEventReader(File file) {
		this(new MessageToEventIterator(new ZimReader(file).iterator()));
	}

	public MessageEventReader(InputStream is) {
		this(new MessageToEventIterator(new ZimReader(is).iterator()));
	}

	public MessageEventReader(String text) {
		this(new MessageToEventIterator(new MessageReader(text).iterator()));
	}

	public MessageEventReader(MessageEvent... events) {
		this(stream(events));
	}

	public MessageEventReader(List<MessageEvent> events) {
		this(events.stream());
	}

	public MessageEventReader(Stream<MessageEvent> stream) {
		this(stream.sorted().iterator());
	}

	public MessageEventReader(Iterator<MessageEvent> iterator) {
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public MessageEvent next() {
		return iterator.next();
	}

	@Override
	public void close() throws Exception {
		if(iterator instanceof AutoCloseable) ((AutoCloseable) iterator).close();
	}

	private static class MessageToEventIterator implements Iterator<MessageEvent>, AutoCloseable {

		private final Iterator<Message> source;

		public MessageToEventIterator(Iterator<Message> source) {
			this.source = source;
		}

		@Override
		public void close() throws Exception {
			if(source instanceof AutoCloseable) ((AutoCloseable) source).close();
		}

		@Override
		public boolean hasNext() {
			return source.hasNext();
		}

		@Override
		public MessageEvent next() {
			return new MessageEvent(source.next());
		}
	}
}
