package io.intino.alexandria.message.parser;

import io.intino.alexandria.message.MessageException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
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

	public int nextLines(String[] lines) {
		try {
			if(buffer.length() == 0) return 0;
			buffer.setLength(buffer.length() - 1);
			lines[0] = buffer.toString();
			int i = 1;
			String next;
			while((next = reader.readLine()) != null && isNotANewMessage(next)) {
				addLine(next, i++, lines);
			}
			buffer.setLength(0);
			if(next != null) {
				buffer.append(next).append('\n');
			} else {
				reader.close();
			}
			return i;
		} catch (Exception e) {
			throw new MessageException(e.getMessage(), e);
		}
	}

	private void addLine(String next, int i, String[] lines) {
		if(i >= lines.length) lines = Arrays.copyOf(lines, lines.length * 2);
		lines[i] = next;
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
			String line = getFirstLineNonBlank();
			if(line != null) buffer.append(line).append('\n');
			else reader.close();
		} catch (IOException ignored) {}
	}

	private String getFirstLineNonBlank() throws IOException {
		String line = reader.readLine();
		while(line != null && line.isBlank()) line = reader.readLine();
		return line;
	}
}
