package io.intino.alexandria.message;

import io.intino.alexandria.message.parser.MessageStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class FastMessageReader implements Iterator<Message>, Iterable<Message>, AutoCloseable {

	private final MessageStream messageStream;
	private final String[] lines;
	private int numLines;
	private final int contextMaxLevels;

	public FastMessageReader(String str) {
		this(new ByteArrayInputStream(str.getBytes()), new Config());
	}

	public FastMessageReader(String str, Config config) {
		this(new ByteArrayInputStream(str.getBytes()), config);
	}

	public FastMessageReader(InputStream inputStream) {
		this(new MessageStream(inputStream), new Config());
	}

	public FastMessageReader(InputStream inputStream, Config config) {
		this(new MessageStream(inputStream), config);
	}

	public FastMessageReader(MessageStream messageStream) {
		this(messageStream, new Config());
	}

	public FastMessageReader(MessageStream messageStream, Config config) {
		this.messageStream = messageStream;
		this.lines = new String[config.linesBufferSize];
		this.contextMaxLevels = config.contextMaxLevels;
		init();
	}

	@Override
	public boolean hasNext() {
		return numLines > 0;
	}

	@Override
	public Message next() {
		try {
			return hasNext() ? nextMessage() : null;
		} catch (Exception e) {
			throw new MessageException(e.getMessage(),e);
		}
	}

	@Override
	public Iterator<Message> iterator() {
		return this;
	}

	@Override
	public void close() throws Exception {
		try {
			messageStream.close();
		} catch (IOException e) {
			Logger.getGlobal().severe(e.getMessage());
		}
	}

	private Message nextMessage() {
		Message[] messages = new Message[contextMaxLevels];
		parse(lines, numLines, messages);
		while((numLines = messageStream.nextLines(lines)) > 0 && isComponent(lines[0])) {
			parse(lines, numLines, messages);
		}
		return firstNonNull(messages);
	}

	private Message firstNonNull(Message[] messages) {
		for(Message m : messages) if(m != null) return m;
		throw new NoSuchElementException("No messages left");
	}

	private void parse(String[] lines, int size, Message[] contextList) {
		String type = typeOf(lines[0]);
		lines[0] = null;
		String[] context = type.split("\\.", -1);
		final int level = context.length - 1;
		Message message = new Message(context[level]);
		if(level > 0 && contextList[level - 1] != null) {
			contextList[level - 1].add(message);
		}
		contextList[level] = message;
		readAttributes(message, lines, size);
	}

	private void readAttributes(Message message, String[] lines, int size) {
		for(int i = 1;i < size;i++) {
			String line = lines[i];
			lines[i] = null;
			if(line.isBlank()) continue;
			int attribSep = line.indexOf(':');
			String name = line.substring(0, attribSep);
			String value = line.substring(attribSep + 1).trim();
			if(!value.isEmpty()) {
				message.set(name, value);
				continue;
			}
			i = readMultilineAttribute(message, lines, size, i, name);
		}
	}

	private static int readMultilineAttribute(Message message, String[] lines, int size, int i, String name) {
		String line;
		StringBuilder multilineValue = new StringBuilder(128);
		for(i = i + 1; i < size; i++) {
			line = lines[i];
			lines[i] = null;
			if(!line.startsWith("\t")) {
				setMultilineAttribute(message, name, multilineValue);
				return i;
			}
			multilineValue.append(line.substring(1)).append('\n');
		}
		setMultilineAttribute(message, name, multilineValue);
		return i;
	}

	private static void setMultilineAttribute(Message message, String name, StringBuilder multilineValue) {
		multilineValue.setLength(Math.max(0, multilineValue.length() - 1));
		message.set(name, multilineValue.toString());
	}

	private String typeOf(String line) {
		return line.substring(1, line.lastIndexOf(']'));
	}

	private boolean isComponent(String next) {
		return next.indexOf('.') > 0;
	}

	private void init() {
		if (this.messageStream.hasNext())
			this.numLines = this.messageStream.nextLines(lines);
	}

	public static class Config {
		private int linesBufferSize = 128;
		private int contextMaxLevels = 16;

		public Config linesBufferSize(int linesBufferSize) {
			this.linesBufferSize = linesBufferSize;
			return this;
		}

		public Config contextMaxLevels(int contextMaxLevels) {
			this.contextMaxLevels = contextMaxLevels;
			return this;
		}
	}
}