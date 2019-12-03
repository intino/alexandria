package io.intino.alexandria.event;

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
	private final Iterator<Event> iterator;
	private Event current;


	public EventReader(File file) {
		this(iteratorOf(new ZimReader(file).iterator()));
	}

	public EventReader(InputStream is) {
		this(iteratorOf(new ZimReader(is).iterator()));
	}

	public EventReader(String text) {
		this(iteratorOf(new ZimReader(text).iterator()));
	}

	public EventReader(Event... events) {
		this(stream(events));
	}

	public EventReader(List<Event> events) {
		this(events.stream());
	}

	public EventReader(Stream<Event> stream) {
		this(stream.sorted(comparing(Event::ts)).iterator());
	}

	public EventReader(Iterator<Event> iterator) {
		this.iterator = iterator;
	}

	@Override
	public Event current() {
		return current;
	}

	@Override
	public Event next() {
		return current = iterator.hasNext() ? iterator.next() : null;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	private static Iterator<Event> iteratorOf(Iterator<Message> iterator) {
		return new Iterator<Event>() {
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public Event next() {
				return new Event(iterator.next());
			}
		};
	}
}
