package io.intino.alexandria.message;

import io.intino.alexandria.message.parser.InlGrammar;
import io.intino.alexandria.message.parser.InlLexicon;
import io.intino.alexandria.message.parser.MessageGenerator;
import io.intino.alexandria.message.parser.MessageStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
			String builder = nextTextMessage().toString();
			return nextMessage(builder);
		} catch (Exception e) {
			return null;
		}
	}

	private StringBuilder nextTextMessage() {
		StringBuilder builder = new StringBuilder(current);
		String next;
		while ((next = messageStream.next()) != null && isComponent(next)) builder.append("\n").append(next);
		current = next;
		return builder;
	}

	private boolean isComponent(String next) {
		return next.substring(0, next.indexOf('\n')).contains(".");
	}

	private Message nextMessage(String next) {
//		Lexer lexer = new InlLexicon(CharStreams.fromString(next));
//		lexer.reset();
		List<Token> lexicon = lexicon(next);
//		InlGrammar.RootContext root = new InlGrammar(new CommonTokenStream(lexer)).root();
//		MessageGenerator generator = new MessageGenerator();
//		new ParseTreeWalker().walk(generator, root);
//		Message next1 = generator.next();
		return null;
	}

	private List<Token> lexicon(String text) {
		InlLexicon lexer = null;
		try {
			lexer = new InlLexicon(CharStreams.fromStream(new ByteArrayInputStream(text.getBytes())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		lexer.reset();
		List<Token> tokens = new ArrayList<>();
		Token token;
		while ((token = lexer.nextToken()).getType() != -1) tokens.add(token);
		return tokens;
	}
}