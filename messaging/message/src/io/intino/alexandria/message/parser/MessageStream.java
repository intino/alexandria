package io.intino.alexandria.message.parser;

import io.intino.alexandria.message.MessageException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Iterator;

public class MessageStream implements Iterator<String>, AutoCloseable {
	private final BufferedReader reader;
	private int last = 0;

	public MessageStream(InputStream stream) {
		this(stream, Charset.defaultCharset());
	}

	public MessageStream(InputStream stream, Charset charset) {
		reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(stream), charset));
		init();
	}

	@Override
	public boolean hasNext() {
		return last != -1;
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	@Override
	public String next() {
		if (last == -1) return null;
		StringBuilder builder = new StringBuilder("[");
		try {
			char current;
			int r;
			while ((r = reader.read()) != -1 && (last != '\n' || r != '[')) {
				current = (char) r;
				builder.append(current);
				last = current;
			}
			last = r;
		} catch (IOException e) {
			throw new MessageException(e.getMessage(), e);
		}
		return builder.length() == 1 ? "" : builder.toString().replace("\r\n", "\n");
	}

	private void init() {
		try {
			reader.read();
		} catch (IOException ignored) {
		}
	}
}
