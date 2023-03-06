package io.intino.test;

import io.intino.alexandria.message.MessageReader;
import io.intino.alexandria.message.parser.InlLexicon;
import io.intino.performance.impl.MessageStreamOld;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.VocabularyImpl;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Parser_ {
	@Test
	public void should_split_messages() {
		MessageStreamOld stream = new MessageStreamOld(new ByteArrayInputStream(inl1.getBytes()), StandardCharsets.UTF_8);
		while (stream.hasNext()) {
			System.out.println(stream.next());
			System.out.println();
			System.out.println();
		}
		stream = new MessageStreamOld(new ByteArrayInputStream(inl2.getBytes()), StandardCharsets.UTF_8);
		while (stream.hasNext()) {
			System.out.println(stream.next());
			System.out.println();
			System.out.println();
		}
	}

	@Test
	public void simple_message() throws IOException {
		showLexicon(inl1);
		messagesFrom(inl1);
	}

	@Test
	public void should_read_message_with_components() throws IOException {
		showLexicon(inl2);
		messagesFrom(inl2);
	}

	@Test
	public void should_read_message_with_multiline_attribute() throws IOException {
		showLexicon(inl3);
		messagesFrom(inl3);
	}


	private void messagesFrom(String input) {
		MessageReader reader = new MessageReader(input);
		while (reader.hasNext()) {
			System.out.println(reader.next());
			System.out.println("------------------------------");
		}
	}

	private void showLexicon(String text) throws IOException {
		InlLexicon lexer = new InlLexicon(CharStreams.fromStream(new ByteArrayInputStream(text.getBytes())));
		lexer.reset();
		Token token;
		while ((token = lexer.nextToken()).getType() != -1) {
			String name = tokenName(lexer, token);
			if (name.contains("LINE")) System.out.println();
			System.out.print(name + ", ");
		}
		System.out.println();
	}

	private String tokenName(InlLexicon lexer, Token token) {
		if (token.getType() == InlLexicon.VALUE) return "VALUE";
		String symbolicName = lexer.getVocabulary().getSymbolicName(token.getType());
		return symbolicName == null ? String.valueOf(token.getType()) : symbolicName;
	}

	private String getExpectedTokens(Parser recognizer) {
		try {
			return recognizer.getExpectedTokens().toString(VocabularyImpl.fromTokenNames(recognizer.getTokenNames()));
		} catch (Exception e) {
			return "";
		}
	}


	String inl1 = "[Teacher]\n" +
			"name: Jose\n" +
			"money: 50.0\n" +
			"birthDate: 1984-11-01T22:34:25Z\n" +
			"university: ULPGC\n";
	String inl2 = "[Teacher]\n" +
			"name: Jose\n" +
			"money: 50.0\n" +
			"birthDate: 1984-11-01T22:34:25Z\n" +
			"university: ULPGC\n" +
			"\n" +
			"[Teacher.Country]\n" +
			"name: Spain\n" +
			"\n" +
			"[Teacher]\n" +
			"name: Juan\n" +
			"money: 40.0\n" +
			"birthDate: 1978-04-02T00:00:00Z\n" +
			"university: ULL\n" +
			"\n" +
			"[Teacher.Country]\n" +
			"name: France\n" +
			"\n" +
			"[Teacher.Country]\n" +
			"name: Germany\n";

	String inl3 = "[ERROR]\n" +
			"ts: 2020-05-05T19:24:32.533342Z\n" +
			"source: io.intino.alexandria.jms.TopicConsumer:close:36\n" +
			"message:\n" +
			"\tjavax.jms.IllegalStateException: The Session is closed\n" +
			"\t\tat org.apache.activemq.ActiveMQSession.checkClosed(ActiveMQSession.java:771)\n" +
			"\t\tat java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515)\n" +
			"\t\tat java.base/java.util.concurrent.FutureTask.runAndReset(FutureTask.java:305)\n" +
			"\t\tat java.base/java.lang.Thread.run(Thread.java:834)\n" +
			"addiotionalInfo: during execution\n";

}
