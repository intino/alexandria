package io.intino.alexandria.message.parser;

import io.intino.alexandria.message.exceptions.ErrorStrategy;
import io.intino.alexandria.message.exceptions.GrammarErrorListener;
import io.intino.alexandria.message.exceptions.SyntaxException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.antlr.v4.runtime.CharStreams.fromString;

public class InlParser {

	private final String input;
	private final ResourceBundle messages;
	private InlGrammar grammar;
	private InlGrammar.RootContext rootContext;

	public InlParser(String input) {
		this.input = input;
		this.messages = ResourceBundle.getBundle("messages", Locale.getDefault(), new UTF8Control());
		if (input == null || input.isEmpty()) return;
		Lexer lexer = new InlLexicon(fromString(this.input));
		lexer.reset();
		this.grammar = new InlGrammar(new CommonTokenStream(lexer));
		this.grammar.setErrorHandler(new ErrorStrategy());
		this.grammar.addErrorListener(new GrammarErrorListener());
	}

	public void parse() throws SyntaxException {
		try {
			rootContext = grammar.root();

		} catch (RecognitionException e) {
			Parser recognizer = (Parser) e.getRecognizer();
			Token token = recognizer.getCurrentToken();
			throw new SyntaxException().add(new SyntaxException.SyntaxError(messages, token.getLine(), token.getCharPositionInLine(), token.getText(), getExpectedTokens(recognizer)));
		} catch (NullPointerException ignored) {
			ignored.printStackTrace();
		}
	}
	private void walk(ParseTreeWalker walker, MesssageGenerator generator) throws SyntaxException, SemanticException {
		try {
			if (rootContext == null) return;
			walker.walk(generator, rootContext);
			if (!generator.errors().isEmpty()) throw new SemanticException().addAll(generator.errors());
		} catch (RecognitionException e) {
			Token token = ((Parser) e.getRecognizer()).getCurrentToken();
			throw new SyntaxException().add(new SyntaxException.SyntaxError(messages, token.getLine(), token.getCharPositionInLine(), token.getText(), getExpectedTokens((Parser) e.getRecognizer())));
		}

	}

	private String getExpectedTokens(Parser recognizer) {
		try {
			return recognizer.getExpectedTokens().toString(VocabularyImpl.fromTokenNames(recognizer.getTokenNames()));
		} catch (Exception e) {
			return "";
		}
	}

}
