package io.intino.alexandria.message.parser;

import io.intino.alexandria.message.MessageException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Iterator;

public class MessageStream implements Iterator<String>, AutoCloseable {

	private final BufferedReader reader;
	private final StringBuilder buffer;

	public MessageStream(InputStream stream) {
		this(stream, Charset.defaultCharset());
	}

	public MessageStream(InputStream stream, Charset charset) {
		this.reader = new BufferedReader(new InputStreamReader(stream, charset));
		this.buffer = new StringBuilder(256);
		init();
	}

	@Override
	public boolean hasNext() {
		return buffer.length() != 0;
	}

	@Override
	public String next() {
		try {
			if(buffer.length() == 0) return null;
			final BufferedReader reader = this.reader;
			final StringBuilder buffer = this.buffer;
			String next;
			while((next = reader.readLine()) != null && isNotANewMessage(next)) {
				buffer.append(next).append('\n');
			}
			final String message = buffer.toString();
			buffer.setLength(0);
			if(next != null) {
				buffer.append(next).append('\n');
			} else {
				reader.close();
			}
			return message;
		} catch (Exception e) {
			throw new MessageException(e.getMessage(), e);
		}
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	private static boolean isNotANewMessage(String next) {
		return next.isEmpty() || next.charAt(0) != '[';
	}

	private void init() {
		try {
			String line = reader.readLine();
			if(line != null) buffer.append(line).append('\n');
			else reader.close();
		} catch (IOException ignored) {}
	}
}
