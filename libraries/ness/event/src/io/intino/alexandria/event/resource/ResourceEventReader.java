package io.intino.alexandria.event.resource;

import io.intino.alexandria.Resource;
import io.intino.alexandria.event.EventReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;

public class ResourceEventReader implements EventReader<ResourceEvent> {

	private final Iterator<ResourceEvent> iterator;

	public ResourceEventReader(File file) throws IOException {
		this(new ResourceToEventIterator(new ZipResourceReader(file)));
	}

	public ResourceEventReader(InputStream inputStream) throws IOException {
		this(new ResourceToEventIterator(new ZipResourceReader(inputStream)));
	}

	public ResourceEventReader(String text) throws IOException {
		this(new ResourceToEventIterator(new ZipResourceReader(new ByteArrayInputStream(text.getBytes(UTF_8)))));
	}

	public ResourceEventReader(ResourceEvent... events) {
		this(stream(events));
	}

	public ResourceEventReader(List<ResourceEvent> events) {
		this(events.stream());
	}

	public ResourceEventReader(Stream<ResourceEvent> stream) {
		this(stream.sorted().iterator());
	}

	public ResourceEventReader(Iterator<ResourceEvent> iterator) {
		this.iterator = iterator;
	}

	@Override
	public void close() throws Exception {
		if(iterator instanceof AutoCloseable) ((AutoCloseable) iterator).close();
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public ResourceEvent next() {
		return iterator.next();
	}

	public static class ResourceToEventIterator implements Iterator<ResourceEvent> {

		private final Iterator<Resource> iterator;

		public ResourceToEventIterator(Iterator<Resource> iterator) {
			this.iterator = iterator;
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public ResourceEvent next() {
			return ResourceEvent.of(iterator.next());
		}
	}
}
