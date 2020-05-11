package io.intino.alexandria.message.parser.exceptions;

import org.antlr.v4.runtime.*;

public class ErrorStrategy extends DefaultErrorStrategy {

	private static Token currentError;


	@Override
	public Token recoverInline(Parser recognizer) {
		reportError(recognizer, new InputMismatchException(recognizer));
		return null;
	}

	@Override
	protected void reportNoViableAlternative(Parser recognizer, NoViableAltException e) {
		reportError(recognizer, null);
	}

	@Override
	protected void reportInputMismatch(Parser recognizer, InputMismatchException e) {
		reportError(recognizer, null);
	}

	@Override
	protected void reportFailedPredicate(Parser recognizer, FailedPredicateException e) {
		reportError(recognizer, null);
	}

	@Override
	protected void reportUnwantedToken(Parser recognizer) {
		reportError(recognizer, null);
	}

	@Override
	protected void reportMissingToken(Parser recognizer) {
		reportError(recognizer, null);
	}

	public void reportError(Parser recognizer, RecognitionException e) {
		printParameters(recognizer);
		throw new InputMismatchException(recognizer);
	}

	private void printParameters(Parser recognizer) {
		Token token = recognizer.getCurrentToken();
		if (currentError == token) return;
		else currentError = token;
		String[] nameList = recognizer.getTokenNames();
		System.out.println("Line: " + token.getLine() + "\n" +
				"Column: " + token.getCharPositionInLine() + "\n" +
				"Text Length: " + token.getText().length() + "\n" +
				(token.getType() > 0 ? "Token type: " + nameList[token.getType()] + "\n" : "") +
				"Expected tokens: " + recognizer.getExpectedTokens().toString(recognizer.getTokenNames()) +
				"\nText: " + token.getText().replace("\n", "\\n"));
	}
}