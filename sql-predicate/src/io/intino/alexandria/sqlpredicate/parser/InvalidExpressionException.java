package io.intino.alexandria.sqlpredicate.parser;

public class InvalidExpressionException extends Exception {
	private static final long serialVersionUID = 6223038613086963841L;

	public InvalidExpressionException(String reason) {
		super(reason);
	}
}
