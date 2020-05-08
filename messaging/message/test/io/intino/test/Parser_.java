package io.intino.test;

import io.intino.alexandria.message.exceptions.SyntaxException;
import io.intino.alexandria.message.parser.InlLexicon;
import io.intino.alexandria.message.parser.InlParser;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.VocabularyImpl;
import org.junit.Assert;
import org.junit.Test;

import static org.antlr.v4.runtime.CharStreams.fromString;

public class Parser_ {
	@Test
	public void simple_message() {
		String inl = "[Teacher]\n" +
				"name: Jose\n" +
				"money: 50.0\n" +
				"birthDate: 1984-11-01T22:34:25Z\n" +
				"university: ULPGC\n";
		try {
			showLexicon(inl);
			checkGrammar(inl);
		} catch (SyntaxException e) {
			Assert.fail("'error'   ->    " + e.errors().get(0).message());
		}
	}


	@Test
	public void should_read_messages_in_a_class_with_parent() {
		String inl = "[Teacher]\n" +
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
		try {
			showLexicon(inl);
			checkGrammar(inl);
		} catch (SyntaxException e) {
			Assert.fail("'error'   ->    " + e.errors().get(0).message());
		}
	}


	private String textFromKey(String key) {
		return key;
	}

	private InlLexicon showLexicon(String text) {
		InlLexicon lexer = new InlLexicon(fromString(text));
		lexer.reset();
		Token token;
		while ((token = lexer.nextToken()).getType() != -1) {
			String name = tokenName(lexer, token);
			if (name.contains("LINE")) System.out.println();
			System.out.print(name + ", ");
		}
		System.out.println();
		return lexer;
	}

	private String tokenName(InlLexicon lexer, Token token) {
		if (token.getType() == 21) return "C";
		return lexer.getVocabulary().getSymbolicName(token.getType());
	}

	private void checkGrammar(String input) throws SyntaxException {
		InlParser parser = new InlParser(input);
		parser.parse();
	}

	private String getExpectedTokens(Parser recognizer) {
		try {
			return recognizer.getExpectedTokens().toString(VocabularyImpl.fromTokenNames(recognizer.getTokenNames()));
		} catch (Exception e) {
			return "";
		}
	}
}
