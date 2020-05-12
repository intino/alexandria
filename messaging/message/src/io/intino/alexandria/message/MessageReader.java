package io.intino.alexandria.message;

import io.intino.alexandria.message.parser.InlLexicon;
import io.intino.alexandria.message.parser.MessageStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static io.intino.alexandria.message.parser.InlLexicon.*;

public class MessageReader implements Iterator<Message> {
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
			if (current == null) return null;
			return nextMessage();
		} catch (Exception e) {
			return null;
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
		StringBuilder idb = new StringBuilder();
		while (iterator.hasNext()) {
			current = iterator.next();
			if (current.getType() == RSQUARE) break;
			else idb.append(current.getText());
		}
		iterator.next();
		String id = idb.toString();
		String[] contexts = id.split("\\.");
		Message message = new Message(contexts[contexts.length - 1]);
		loadAttributes(message, iterator);
		return new AbstractMap.SimpleEntry<>(contexts.length - 1, message);
	}

	private void loadAttributes(Message message, Iterator<Token> iterator) {
		while (iterator.hasNext()) {
			Token id = iterator.next();
			if (id.getType() == NEWLINE) break;
			String attributeName = id.getText();
			iterator.next();
			Token next = iterator.next();
			String value;
			if (next.getType() == VALUE) {
				value = next.getText();
				iterator.next();
			} else value = multiline(iterator, next);
			message.set(attributeName, value.trim());
		}
	}

	private String multiline(Iterator<Token> iterator, Token last) {
		Token current = last;
		StringBuilder builder = new StringBuilder();
		while (iterator.hasNext() && current.getType() != NEWLINE) {
			current = iterator.next();
			if (current.getType() == VALUE) builder.append(current.getText()).append("\n");
		}
		return builder.toString();
	}

	private List<Token> lexicon(String text) {
		InlLexicon lexer = new InlLexicon(CharStreams.fromString(text));
		lexer.reset();
		List<Token> tokens = new ArrayList<>();
		Token token;
		while ((token = lexer.nextToken()).getType() != -1) tokens.add(token);
		return tokens;
	}
}