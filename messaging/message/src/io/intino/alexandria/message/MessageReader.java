package io.intino.alexandria.message;

import io.intino.alexandria.message.parser.InlLexicon;
import io.intino.alexandria.message.parser.MessageStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

import static io.intino.alexandria.message.parser.InlLexicon.*;

public class MessageReader implements Iterator<Message>, Iterable<Message> {
	private final MessageStream messageStream;
	private String current;

	public MessageReader(String str) {
		this(new ByteArrayInputStream(str.getBytes()));
	}

	public MessageReader(InputStream inputStream) {
		messageStream = new MessageStream(inputStream);
		if (messageStream.hasNext()) current = messageStream.next();
	}

	public boolean hasNext() {
		return current != null && !current.isEmpty() && !current.isBlank();
	}

	public Message next() {
		try {
			if (current == null || current.isEmpty() || current.isBlank()) return null;
			return nextMessage();
		} catch (Exception e) {
			Logger.getGlobal().severe(e.getMessage() + ":" + current);
			current = null;
			return null;
		}
	}

	public void close() {
		try {
			messageStream.close();
		} catch (IOException e) {
			Logger.getGlobal().severe(e.getMessage());
		}
	}

	private Message nextMessage() {
		List<Message> messageContexts = new ArrayList<>();
		messageContexts.add(nextMessage(current).getValue());
		String next;
		while ((next = messageStream.next()) != null && isComponent(next)) {
			Map.Entry<Integer, Message> entry = nextMessage(next);
			add(messageContexts, entry.getKey(), entry.getValue());
			messageContexts.get(entry.getKey() - 1).add(entry.getValue());
		}
		current = next;
		return messageContexts.get(0);
	}

	private void add(List<Message> messageContexts, Integer level, Message value) {
		if (messageContexts.size() <= level) messageContexts.add(level, value);
		else messageContexts.set(level, value);
	}

	private boolean isComponent(String next) {
		return next.substring(0, next.indexOf('\n')).contains(".");
	}

	private Map.Entry<Integer, Message> nextMessage(String next) {
		return getNextMessage(lexicon(next).iterator());
	}

	private Map.Entry<Integer, Message> getNextMessage(Iterator<Token> iterator) {
		Token current;
		while (iterator.hasNext()) {
			current = iterator.next();
			if (current.getType() == LSQUARE) break;
		}
		String type = readType(iterator);
		iterator.next();
		String[] contexts = type.split("\\.");
		Message message = new Message(contexts[contexts.length - 1]);
		readAttributes(message, iterator);
		return new AbstractMap.SimpleEntry<>(contexts.length - 1, message);
	}

	private String readType(Iterator<Token> iterator) {
		StringBuilder idb = new StringBuilder();
		Token current;
		while (iterator.hasNext()) {
			current = iterator.next();
			if (current.getType() == RSQUARE) break;
			else idb.append(current.getText());
		}
		return idb.toString();
	}

	private void readAttributes(Message message, Iterator<Token> iterator) {
		while (iterator.hasNext()) {
			Token id = iterator.next();
			if (id.getType() == NEWLINE) break;
			String attributeName = id.getText();
			iterator.next();
			Token next = iterator.next();
			String value;
			if (isInLineValue(next)) {
				value = next.getText().substring(1);
				if (iterator.hasNext()) iterator.next();
			} else value = multiline(iterator, next);
			message.set(attributeName, value);
		}
	}

	private boolean isInLineValue(Token next) {
		return next.getType() == VALUE;
	}

	private String multiline(Iterator<Token> iterator, Token last) {
		Token current = last;
		StringBuilder builder = new StringBuilder();
		while (iterator.hasNext() && current.getType() != NEWLINE) {
			current = iterator.next();
			if (current.getType() == VALUE) builder.append("\n").append(current.getText());
		}
		return builder.length() == 0 ? "" : builder.toString().substring(1);
	}

	private List<Token> lexicon(String text) {
		InlLexicon lexer = new InlLexicon(CharStreams.fromString(text));
		return (List<Token>) lexer.getAllTokens();
	}

	@Override
	public Iterator<Message> iterator() {
		return this;
	}
}