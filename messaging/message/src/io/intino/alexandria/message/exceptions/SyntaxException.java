package io.intino.alexandria.message.exceptions;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class SyntaxException extends InlException {

	public SyntaxException add(SyntaxError error) {
		errors.add(error);
		return this;
	}

	public static class SyntaxError extends InlError {

		private final ResourceBundle errorMessages;
		private final String currentToken;
		private final String expectedTokens;

		public SyntaxError(ResourceBundle errorMessages, int line, int column, String currentToken, String expectedTokens) {
			super(line, column, "Syntax error" + (!expectedTokens.isEmpty() ? ". Expected " + expectedTokens : "") + " on line " + line + " and column " + column);
			this.errorMessages = errorMessages;
			this.currentToken = currentToken.equalsIgnoreCase("<EOF>") ? message("ending") : currentToken;
			this.expectedTokens = expectedTokens;
		}

		public String lineMessage() {
			return expectedTokens.isEmpty() ? message("syntax.error", currentToken, column) : message("syntax.error.with.expected", currentToken, expectedTokens, column);
		}

		private String message(String key, Object... parameters) {
			return MessageFormat.format(new String(errorMessages.getString(key).getBytes(), StandardCharsets.UTF_8), parameters);
		}
	}

	@Override
	public String getMessage() {
		StringBuilder builder = new StringBuilder();
		for (InlError error : errors) {
			builder.append(error.message()).append("\n");
		}
		return builder.toString();
	}
}