package io.intino.alexandria.ztp;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.resourcecleaner.DisposableResource;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static io.intino.alexandria.resourcecleaner.DisposableResource.whenDestroyed;
import static io.intino.alexandria.ztp.Tuple.TUPLE_SEPARATOR;

public class TupleReader implements Iterator<Tuple>, Iterable<Tuple>, AutoCloseable {

	private final BufferedReader reader;
	private final DisposableResource resource;
	private String currentLine;

	public TupleReader(String text) {
		this(text, Charset.defaultCharset());
	}

	public TupleReader(String text, Charset charset) {
		this(new ByteArrayInputStream(text.getBytes(charset)));
	}

	public TupleReader(InputStream input) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		this.reader = reader;
		this.resource = whenDestroyed(this).thenClose(reader);
		advance();
	}

	@Override
	public boolean hasNext() {
		return currentLine != null;
	}

	@Override
	public Tuple next() {
		Tuple tuple = parseTupleFrom(currentLine);
		advance();
		return tuple;
	}

	@Override
	public Iterator<Tuple> iterator() {
		return this;
	}

	@Override
	public void close() {
		resource.close();
	}

	private Tuple parseTupleFrom(String currentLine) {
		if(currentLine == null) throw new NoSuchElementException("No more elements!");
		return new Tuple(currentLine.split(TUPLE_SEPARATOR, -1));
	}

	private void advance() {
		try {
			do {
				currentLine = reader.readLine();
			} while(currentLine != null && currentLine.isEmpty());
		} catch (IOException e) {
			Logger.error(e);
		}
	}
}
