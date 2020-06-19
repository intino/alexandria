package io.intino.alexandria.message.parser;

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
		StringBuilder builder = new StringBuilder();
		try {
			char current = 0;
			int r;
			while ((r = reader.read()) != -1 && (last != '\n' || r != '[')) {
				current = (char) r;
				builder.append(current);
				last = current;
			}
			last = r;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (builder.length() > 0) builder.insert(0, '[');
		return builder.toString().replace("\r\n", "\n");
	}

	private void init() {
		try {
			reader.read();
		} catch (IOException e) {
		}
	}
}
