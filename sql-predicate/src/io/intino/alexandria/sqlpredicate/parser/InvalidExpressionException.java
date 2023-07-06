package io.intino.alexandria.sqlpredicate.parser;

public class InvalidExpressionException extends Exception {
	public InvalidExpressionException(String reason) {
		super(reason);
	}
}
