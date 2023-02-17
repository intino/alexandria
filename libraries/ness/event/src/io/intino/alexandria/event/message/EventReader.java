package io.intino.alexandria.event.message;

import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.zim.ZimReader;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;

public class EventReader implements EventStream {
	private final Iterator<MessageEvent> iterator;
	private MessageEvent current;


	public EventReader(File file) {
		this(iteratorOf(new ZimReader(file).iterator()));
	}

	public EventReader(InputStream is) {
		this(iteratorOf(new ZimReader(is).iterator()));
	}

	public EventReader(String text) {
		this(iteratorOf(new ZimReader(text).iterator()));
	}

	public EventReader(MessageEvent... events) {
		this(stream(events));
	}

	public EventReader(List<MessageEvent> events) {
		this(events.stream());
	}

	public EventReader(Stream<MessageEvent> stream) {
		this(stream.sorted(comparing(MessageEvent::ts)).iterator());
	}

	public EventReader(Iterator<MessageEvent> iterator) {
		this.iterator = iterator;
	}

	@Override
	public MessageEvent current() {
		return current;
	}

	@Override
	public MessageEvent next() {
		return current = iterator.hasNext() ? iterator.next() : null;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	private static Iterator<MessageEvent> iteratorOf(Iterator<Message> iterator) {
		return new Iterator<>() {
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public MessageEvent next() {
				return new MessageEvent(iterator.next());
			}
		};
	}
}
