package io.intino.alexandria.message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

/**
 * MessageReader that supports backwards compatibility with old multiline attributes. May be removed in the future.
 * */
public class LegacyMessageReader implements Iterator<Message>, Iterable<Message>, AutoCloseable {

	private static final String MULTILINE_ATTRIBUTE_PREFIX = "\t";

	private final MessageStream messageStream;
	private final Message[] contextList;
	private final String[] lines;
	private int numLines;

	public LegacyMessageReader(String str) {
		this(new ByteArrayInputStream(str.getBytes()), new Config());
	}

	public LegacyMessageReader(String str, Config config) {
		this(new ByteArrayInputStream(str.getBytes()), config);
	}

	public LegacyMessageReader(InputStream inputStream) {
		this(new MessageStream(inputStream), new Config());
	}

	public LegacyMessageReader(InputStream inputStream, Config config) {
		this(new MessageStream(inputStream), config);
	}

	public LegacyMessageReader(MessageStream messageStream) {
		this(messageStream, new Config());
	}

	public LegacyMessageReader(MessageStream messageStream, Config config) {
		this.messageStream = messageStream;
		this.lines = new String[Math.max(config.linesBufferSize, 1)];
		this.contextList = new Message[Math.max(config.contextMaxLevels, 1)];
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
		} catch (MessageException e) {
			throw e;
		} catch (Exception e) {
			throw new MessageException(e.getMessage(), e);
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
		parse(lines, numLines, contextList);
		while((numLines = messageStream.nextLines(lines)) > 0 && isComponent(lines[0])) {
			parse(lines, numLines, contextList);
		}
		return firstNonNullAndReset(contextList);
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
			try {
				int attribSep = line.indexOf(':');
				if(attribSep < 0) continue;
				String name = nameOf(line, attribSep);
				String value = valueOf(line, attribSep);

				if(isLegacyMultilineAttribute(value, i, lines)) {
					i = readLegacyMultilineAttribute(message, lines, size, i, name);
				} else {
					message.setUnsafe(name, value);
				}
			} catch (Exception e) {
				throw new MessageException("Malformed message attribute at line " + i + ": " + line, e);
			}
		}
	}

	private String valueOf(String line, int attribSep) {
		int start = line.indexOf(' ', attribSep + 1);
		return line.substring(start < 0 ? attribSep + 1 : start + 1);
	}

	private String nameOf(String line, int attribSep) {
		return line.substring(0, attribSep);
	}

	private boolean isLegacyMultilineAttribute(String value, int i, String[] lines) {
		return value.isEmpty() && (i < lines.length - 1) && lines[i + 1].startsWith(MULTILINE_ATTRIBUTE_PREFIX);
	}

	private static int readLegacyMultilineAttribute(Message message, String[] lines, int size, int i, String name) {
		String line;
		StringBuilder multilineValue = new StringBuilder(128);
		for(i = i + 1; i < size; i++) {
			line = lines[i];
			if(!line.startsWith(MULTILINE_ATTRIBUTE_PREFIX)) {
				setMultilineAttribute(message, name, multilineValue);
				return i - 1;
			}
			multilineValue.append(line.substring(1)).append('\n');
			lines[i] = null;
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

	private Message firstNonNullAndReset(Message[] messages) {
		Message message = null;
		for(int i = 0; i < messages.length; i++) {
			Message m = messages[i];
			if (m != null && message == null) {
				message = m;
			}
			messages[i] = null;
		}
		if(message == null) throw new NoSuchElementException("No messages left");
		return message;
	}

	private void init() {
		if (this.messageStream.hasNext())
			this.numLines = this.messageStream.nextLines(lines);
	}

	public static class Config {
		private int linesBufferSize = 128;
		private int contextMaxLevels = 8;

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